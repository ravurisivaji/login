package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.Chemical;
import com.ravuri.calibration.entity.ChemicalDistribution;
import com.ravuri.calibration.entity.DistributionStatus;
import com.ravuri.calibration.exception.ChemicalNotFoundException;
import com.ravuri.calibration.exception.ChemicalValidationException;
import com.ravuri.calibration.repository.ChemicalDistributionRepository;
import com.ravuri.calibration.repository.ChemicalRepository;
import com.ravuri.calibration.service.ChemicalDistributionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChemicalDistributionServiceImpl implements ChemicalDistributionService {

    private static final Logger LOGGER = LogManager.getLogger(ChemicalDistributionServiceImpl.class);


    @Autowired
    private ChemicalDistributionRepository distributionRepository;
    @Autowired
    private ChemicalRepository chemicalRepository;

    @Override
    @Transactional
    public ChemicalDistribution issueChemical(ChemicalDistribution distribution, String issuedBy) {
        try {
            validateDistribution(distribution);
            Chemical chemical = validateAndGetChemical(distribution.getChemical().getId());
            validateQuantityAvailable(chemical, distribution.getQuantityIssued());

            distribution.setIssuedBy(issuedBy);
            distribution.setIssuedAt(LocalDateTime.now());
            distribution.setStatus(DistributionStatus.ISSUED);
            distribution.setUnit(chemical.getUnit());

            // Update chemical quantity
            chemical.setQuantity(chemical.getQuantity() - distribution.getQuantityIssued());
            chemicalRepository.save(chemical);

            ChemicalDistribution savedDistribution = distributionRepository.save(distribution);
            LOGGER.info("Chemical {} issued to {} by {}", chemical.getName(),
                    distribution.getIssuedTo(), issuedBy);
            return savedDistribution;
        } catch (Exception e) {
            LOGGER.error("Error issuing chemical", e);
            throw new ChemicalValidationException("Failed to issue chemical: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ChemicalDistribution returnChemical(Long distributionId, Double returnedQuantity, String returnedBy) {
        try {
            ChemicalDistribution distribution = getDistributionById(distributionId);
            validateReturn(distribution, returnedQuantity);

            Chemical chemical = distribution.getChemical();
            distribution.setReturnedAt(LocalDateTime.now());
            distribution.setReturnedBy(returnedBy);
            distribution.setQuantityReturned(returnedQuantity);

            if (returnedQuantity.equals(distribution.getQuantityIssued())) {
                distribution.setStatus(DistributionStatus.FULLY_RETURNED);
            } else {
                distribution.setStatus(DistributionStatus.PARTIALLY_RETURNED);
            }

            chemical.setQuantity(chemical.getQuantity() + returnedQuantity);
            chemicalRepository.save(chemical);

            ChemicalDistribution savedDistribution = distributionRepository.save(distribution);
            LOGGER.info("Chemical {} returned by {}, quantity: {}", chemical.getName(),
                    returnedBy, returnedQuantity);
            return savedDistribution;
        } catch (Exception e) {
            LOGGER.error("Error returning chemical", e);
            throw new ChemicalValidationException("Failed to return chemical: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChemicalDistribution> getDistributionsByChemical(Long chemicalId) {
        validateChemicalExists(chemicalId);
        return distributionRepository.findByChemicalId(chemicalId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChemicalDistribution> getDistributionsByDepartment(String department) {
        validateDepartment(department);
        return distributionRepository.findByDepartment(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChemicalDistribution> getDistributionsByLocation(String location) {
        validateLocation(location);
        return distributionRepository.findByLocation(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChemicalDistribution> getDistributionsByDateRange(LocalDateTime start, LocalDateTime end) {
        validateDateRange(start, end);
        return distributionRepository.findByIssuedAtBetween(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChemicalDistribution> getDistributionsByUser(String issuedTo) {
        if (issuedTo == null || issuedTo.trim().isEmpty()) {
            throw new ChemicalValidationException("User ID is required");
        }
        return distributionRepository.findByIssuedTo(issuedTo);
    }

    private ChemicalDistribution getDistributionById(Long id) {
        return distributionRepository.findById(id)
                .orElseThrow(() -> new ChemicalValidationException("Distribution record not found"));
    }

    private Chemical validateAndGetChemical(Long chemicalId) {
        return chemicalRepository.findById(chemicalId)
                .orElseThrow(() -> new ChemicalNotFoundException("Chemical not found"));
    }

    private void validateChemicalExists(Long chemicalId) {
        if (!chemicalRepository.existsById(chemicalId)) {
            throw new ChemicalNotFoundException("Chemical not found");
        }
    }

    private void validateDistribution(ChemicalDistribution distribution) {
        if (distribution.getChemical() == null || distribution.getChemical().getId() == null) {
            throw new ChemicalValidationException("Chemical must be specified");
        }
        if (distribution.getQuantityIssued() == null || distribution.getQuantityIssued() <= 0) {
            throw new ChemicalValidationException("Invalid quantity");
        }
        validateDepartment(distribution.getDepartment());
        validateLocation(distribution.getLocation());
        validateUser(distribution.getIssuedTo());
    }

    private void validateQuantityAvailable(Chemical chemical, Double requestedQuantity) {
        if (chemical.getQuantity() < requestedQuantity) {
            throw new ChemicalValidationException(
                    "Insufficient quantity available. Available: " + chemical.getQuantity());
        }
    }

    private void validateReturn(ChemicalDistribution distribution, Double returnedQuantity) {
        if (distribution.getStatus() == DistributionStatus.FULLY_RETURNED) {
            throw new ChemicalValidationException("Chemical has already been fully returned");
        }
        if (returnedQuantity > distribution.getQuantityIssued()) {
            throw new ChemicalValidationException("Return quantity cannot exceed issued quantity");
        }
        if (returnedQuantity <= 0) {
            throw new ChemicalValidationException("Return quantity must be greater than 0");
        }
    }

    private void validateDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new ChemicalValidationException("Department is required");
        }
    }

    private void validateLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new ChemicalValidationException("Location is required");
        }
    }

    private void validateUser(String user) {
        if (user == null || user.trim().isEmpty()) {
            throw new ChemicalValidationException("User is required");
        }
    }

    private void validateDateRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new ChemicalValidationException("Start and end dates are required");
        }
        if (end.isBefore(start)) {
            throw new ChemicalValidationException("End date cannot be before start date");
        }
    }
}


