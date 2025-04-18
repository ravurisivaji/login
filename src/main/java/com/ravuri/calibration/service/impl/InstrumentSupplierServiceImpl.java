package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.InstrumentSupplier;
import com.ravuri.calibration.entity.SupplierStatus;
import com.ravuri.calibration.exception.SupplierException;
import com.ravuri.calibration.exception.SupplierNotFoundException;
import com.ravuri.calibration.repository.InstrumentSupplierRepository;
import com.ravuri.calibration.service.InstrumentSupplierService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InstrumentSupplierServiceImpl implements InstrumentSupplierService {

    private static final Logger LOGGER = LogManager.getLogger(InstrumentSupplierServiceImpl.class);

    @Autowired
    private InstrumentSupplierRepository supplierRepository;

    @Transactional
    public InstrumentSupplier createSupplier(InstrumentSupplier supplier, String username) {
        try {
            validateSupplier(supplier);
            checkDuplicates(supplier);

            LocalDateTime now = LocalDateTime.now();
            supplier.setCreatedAt(now);
            supplier.setCreatedBy(username);
            supplier.setLastModifiedAt(now);
            supplier.setLastModifiedBy(username);

            InstrumentSupplier savedSupplier = supplierRepository.save(supplier);
            LOGGER.info("Created new supplier: {}", savedSupplier.getName());
            return savedSupplier;
        } catch (Exception e) {
            LOGGER.error("Error creating supplier: {}", e.getMessage());
            throw new SupplierException("Failed to create supplier: " + e.getMessage());
        }
    }

    @Transactional
    public InstrumentSupplier updateSupplier(Long id, InstrumentSupplier supplierDetails, String username) {
        try {
            InstrumentSupplier supplier = getSupplierById(id);
            validateSupplier(supplierDetails);
            checkDuplicatesForUpdate(supplier, supplierDetails);

            updateSupplierFields(supplier, supplierDetails, username);
            InstrumentSupplier updatedSupplier = supplierRepository.save(supplier);
            LOGGER.info("Updated supplier: {}", updatedSupplier.getName());
            return updatedSupplier;
        } catch (Exception e) {
            LOGGER.error("Error updating supplier: {}", e.getMessage());
            throw new SupplierException("Failed to update supplier: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public InstrumentSupplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<InstrumentSupplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<InstrumentSupplier> getSuppliersByStatus(SupplierStatus status) {
        return supplierRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<InstrumentSupplier> getExpiredLicenseSuppliers() {
        return supplierRepository.findByLicenseExpiryDateBefore(LocalDateTime.now());
    }

    private void validateSupplier(InstrumentSupplier supplier) {
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            throw new SupplierException("Supplier name cannot be empty");
        }
        if (supplier.getLicenseNumber() == null || supplier.getLicenseNumber().trim().isEmpty()) {
            throw new SupplierException("License number cannot be empty");
        }
        if (supplier.getLicenseExpiryDate() == null) {
            throw new SupplierException("License expiry date cannot be null");
        }
        if (supplier.getLicenseExpiryDate().isBefore(LocalDateTime.now())) {
            throw new SupplierException("License expiry date cannot be in the past");
        }
        if (supplier.getStatus() == null) {
            throw new SupplierException("Supplier status cannot be null");
        }
    }

    private void checkDuplicates(InstrumentSupplier supplier) {
        if (supplier.getEmail() != null && supplierRepository.existsByEmail(supplier.getEmail())) {
            throw new SupplierException("Supplier with email " + supplier.getEmail() + " already exists");
        }
        if (supplierRepository.existsByLicenseNumber(supplier.getLicenseNumber())) {
            throw new SupplierException("Supplier with license number " + supplier.getLicenseNumber() + " already exists");
        }
    }

    private void checkDuplicatesForUpdate(InstrumentSupplier existing, InstrumentSupplier updated) {
        if (updated.getEmail() != null && !updated.getEmail().equals(existing.getEmail())
                && supplierRepository.existsByEmail(updated.getEmail())) {
            throw new SupplierException("Supplier with email " + updated.getEmail() + " already exists");
        }
        if (!updated.getLicenseNumber().equals(existing.getLicenseNumber())
                && supplierRepository.existsByLicenseNumber(updated.getLicenseNumber())) {
            throw new SupplierException("Supplier with license number " + updated.getLicenseNumber() + " already exists");
        }
    }

    private void updateSupplierFields(InstrumentSupplier supplier, InstrumentSupplier supplierDetails, String username) {
        supplier.setName(supplierDetails.getName());
        supplier.setContactPerson(supplierDetails.getContactPerson());
        supplier.setEmail(supplierDetails.getEmail());
        supplier.setPhone(supplierDetails.getPhone());
        supplier.setAddress(supplierDetails.getAddress());
        supplier.setLicenseNumber(supplierDetails.getLicenseNumber());
        supplier.setLicenseExpiryDate(supplierDetails.getLicenseExpiryDate());
        supplier.setSpecializations(supplierDetails.getSpecializations());
        supplier.setStatus(supplierDetails.getStatus());
        supplier.setLastModifiedAt(LocalDateTime.now());
        supplier.setLastModifiedBy(username);
    }

}
