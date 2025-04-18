package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.Chemical;
import com.ravuri.calibration.entity.ChemicalDistribution;
import com.ravuri.calibration.entity.ChemicalStatus;
import com.ravuri.calibration.entity.DistributionStatus;
import com.ravuri.calibration.exception.ChemicalNotFoundException;
import com.ravuri.calibration.exception.ChemicalValidationException;
import com.ravuri.calibration.repository.ChemicalDistributionRepository;
import com.ravuri.calibration.repository.ChemicalRepository;
import com.ravuri.calibration.service.ChemicalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ravuri.calibration.constant.CalibrationConstants.DATE_FORMATTER;
import static com.ravuri.calibration.constant.CalibrationConstants.TIME_FORMATTER;

@Service
public class ChemicalServiceImpl implements ChemicalService {

    private static final Logger LOGGER = LogManager.getLogger(ChemicalServiceImpl.class);

    @Autowired
    private ChemicalRepository chemicalRepository;

    @Autowired
    private ChemicalDistributionRepository distributionRepository;;

    @Transactional
    public Chemical createChemical(Chemical chemical, String username) {
        try {
            validateChemical(chemical);
            checkDuplicates(chemical);

            LocalDateTime now = LocalDateTime.now();
            String uuid = generateChemicalUUID(chemical.getName(), chemical.getReceivedOn());
            chemical.setBatchNumber(uuid);
            chemical.setCreatedAt(now);
            chemical.setCreatedBy(username);
            chemical.setLastModifiedAt(now);
            chemical.setLastModifiedBy(username);

            Chemical savedChemical = chemicalRepository.save(chemical);
            LOGGER.info("Created new chemical: {} with UUID: {}",
                    savedChemical.getName(), savedChemical.getBatchNumber());
            return savedChemical;
        } catch (Exception e) {
            LOGGER.error("Error creating chemical", e);
            throw new ChemicalValidationException("Failed to create chemical: " + e.getMessage());
        }
    }

    @Transactional
    public Chemical updateChemical(Long id, Chemical chemicalDetails, String username) {
        try {
            Chemical chemical = getChemicalById(id);
            validateChemical(chemicalDetails);
            checkDuplicatesForUpdate(chemical, chemicalDetails);

            updateChemicalFields(chemical, chemicalDetails, username);
            Chemical updatedChemical = chemicalRepository.save(chemical);
            LOGGER.info("Updated chemical: {} with batch number: {}",
                    updatedChemical.getName(), updatedChemical.getBatchNumber());
            return updatedChemical;
        } catch (Exception e) {
            LOGGER.error("Error updating chemical", e);
            throw new ChemicalValidationException("Failed to update chemical: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Chemical getChemicalById(Long id) {
        return chemicalRepository.findById(id)
                .orElseThrow(() -> new ChemicalNotFoundException("Chemical not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Chemical> getAllChemicals() {
        return chemicalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Chemical> getChemicalsByDepartment(String department) {
        validateDepartment(department);
        return chemicalRepository.findByDepartment(department);
    }

    @Transactional(readOnly = true)
    public List<Chemical> getChemicalsByLocation(String location) {
        validateLocation(location);
        return chemicalRepository.findByLocation(location);
    }

    @Transactional(readOnly = true)
    public List<Chemical> getChemicalsByReceiver(String receivedBy) {
        validateReceiver(receivedBy);
        return chemicalRepository.findByReceivedBy(receivedBy);
    }

    @Transactional(readOnly = true)
    public List<Chemical> getExpiredChemicals() {
        return chemicalRepository.findByExpirationDateBefore(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<Chemical> getChemicalsByReceiptDate(LocalDateTime start, LocalDateTime end) {
        validateDateRange(start, end);
        return chemicalRepository.findByReceivedOnBetween(start, end);
    }

    private void validateChemical(Chemical chemical) {
        if (chemical.getName() == null || chemical.getName().trim().isEmpty()) {
            throw new ChemicalValidationException("Chemical name is required");
        }
        if (chemical.getBatchNumber() == null || chemical.getBatchNumber().trim().isEmpty()) {
            throw new ChemicalValidationException("Batch number is required");
        }
        if (chemical.getQuantity() == null || chemical.getQuantity() <= 0) {
            throw new ChemicalValidationException("Quantity must be greater than 0");
        }
        if (chemical.getExpirationDate() == null) {
            throw new ChemicalValidationException("Expiration date is required");
        }
        if (chemical.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new ChemicalValidationException("Expiration date cannot be in the past");
        }
        validateDepartment(chemical.getDepartment());
        validateLocation(chemical.getLocation());
        validateReceiver(chemical.getReceivedBy());
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

    private void validateReceiver(String receivedBy) {
        if (receivedBy == null || receivedBy.trim().isEmpty()) {
            throw new ChemicalValidationException("Receiver information is required");
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

    private void checkDuplicates(Chemical chemical) {
        if (chemicalRepository.existsByBatchNumber(chemical.getBatchNumber())) {
            throw new ChemicalValidationException("Chemical with batch number " +
                    chemical.getBatchNumber() + " already exists");
        }
    }

    private void checkDuplicatesForUpdate(Chemical existing, Chemical updated) {
        if (!updated.getBatchNumber().equals(existing.getBatchNumber()) &&
                chemicalRepository.existsByBatchNumber(updated.getBatchNumber())) {
            throw new ChemicalValidationException("Chemical with batch number " +
                    updated.getBatchNumber() + " already exists");
        }
    }

    private void updateChemicalFields(Chemical chemical, Chemical chemicalDetails, String username) {
        chemical.setName(chemicalDetails.getName());
        chemical.setBatchNumber(chemicalDetails.getBatchNumber());
        chemical.setCasNumber(chemicalDetails.getCasNumber());
        chemical.setQuantity(chemicalDetails.getQuantity());
        chemical.setUnit(chemicalDetails.getUnit());
        chemical.setDepartment(chemicalDetails.getDepartment());
        chemical.setLocation(chemicalDetails.getLocation());
        chemical.setExpirationDate(chemicalDetails.getExpirationDate());
        chemical.setStatus(chemicalDetails.getStatus());
        chemical.setNotes(chemicalDetails.getNotes());
        chemical.setLastModifiedAt(LocalDateTime.now());
        chemical.setLastModifiedBy(username);
    }

    @Transactional
    public ChemicalDistribution issueChemical(ChemicalDistribution distribution, String issuedBy) {
        try {
            validateDistribution(distribution);
            Chemical requestedChemical = validateAndGetChemical(distribution.getChemical().getId());

            // Try to fulfill the request with the requested batch
            if (requestedChemical.getQuantity() >= distribution.getQuantityIssued()) {
                return processChemicalDistribution(distribution, requestedChemical, issuedBy);
            }

            // Find alternative batches if the requested batch has insufficient quantity
            List<Chemical> availableBatches = findAlternativeBatches(requestedChemical, distribution.getQuantityIssued());

            if (!availableBatches.isEmpty()) {
                Chemical alternativeBatch = availableBatches.get(0);
                LOGGER.info("Using alternative batch {} for chemical {}",
                        alternativeBatch.getBatchNumber(), alternativeBatch.getName());

                distribution.setChemical(alternativeBatch);
                return processChemicalDistribution(distribution, alternativeBatch, issuedBy);
            }

            throw new ChemicalValidationException(
                    String.format("Insufficient quantity available for chemical %s. No alternative batches found.",
                            requestedChemical.getName()));

        } catch (Exception e) {
            LOGGER.error("Error issuing chemical", e);
            throw new ChemicalValidationException("Failed to issue chemical: " + e.getMessage());
        }
    }

    private List<Chemical> findAlternativeBatches(Chemical requestedChemical, Double requiredQuantity) {
        return chemicalRepository.findAll().stream()
                .filter(c -> isValidAlternativeBatch(c, requestedChemical, requiredQuantity))
                .sorted(Comparator.comparing(Chemical::getExpirationDate))
                .collect(Collectors.toList());
    }

    private boolean isValidAlternativeBatch(Chemical candidate, Chemical requested, Double requiredQuantity) {
        return !Objects.equals(candidate.getId(), requested.getId()) && // Different batch
                candidate.getName().equals(requested.getName()) && // Same chemical
                candidate.getCasNumber().equals(requested.getCasNumber()) && // Same CAS number
                candidate.getQuantity() >= requiredQuantity && // Sufficient quantity
                candidate.getStatus() == ChemicalStatus.AVAILABLE && // Available for use
                candidate.getExpirationDate().isAfter(LocalDateTime.now()); // Not expired
    }

    private ChemicalDistribution processChemicalDistribution(
            ChemicalDistribution distribution,
            Chemical chemical,
            String issuedBy) {

        distribution.setIssuedBy(issuedBy);
        distribution.setIssuedAt(LocalDateTime.now());
        distribution.setStatus(DistributionStatus.ISSUED);
        distribution.setUnit(chemical.getUnit());

        // Update chemical quantity
        chemical.setQuantity(chemical.getQuantity() - distribution.getQuantityIssued());
        if (chemical.getQuantity() == 0) {
            chemical.setStatus(ChemicalStatus.DEPLETED);
        }
        chemicalRepository.save(chemical);

        ChemicalDistribution savedDistribution = distributionRepository.save(distribution);
        LOGGER.info("Chemical {} (Batch: {}) issued to {} by {}",
                chemical.getName(),
                chemical.getBatchNumber(),
                distribution.getIssuedTo(),
                issuedBy);

        return savedDistribution;
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



    private void validateUser(String user) {
        if (user == null || user.trim().isEmpty()) {
            throw new ChemicalValidationException("User is required");
        }
    }

    private String generateChemicalUUID(String chemicalName, LocalDateTime receivedDate) {
        // Get first 4 characters of chemical name (padded with X if shorter)
        String namePrefix = (chemicalName + "XXXX").substring(0, 4).toUpperCase();

        // Format received date
        String dateStr = receivedDate.format(DATE_FORMATTER);

        // Get current timestamp
        String timeStr = LocalDateTime.now().format(TIME_FORMATTER);

        // Combine all parts to create 32-character UUID
        // Format: CCCC-YYYYMMDD-HHMMSS-RRRRRRRRRRRR
        // where CCCC = chemical prefix, R = random padding
        String randomPadding = String.format("%012d", System.nanoTime() % 1000000000000L);

        return String.format("%s-%s-%s-%s",
                namePrefix, dateStr, timeStr, randomPadding);
    }



}
