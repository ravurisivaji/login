package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.CalibrationInstrument;
import com.ravuri.calibration.entity.NonCalibrationInstrument;
import com.ravuri.calibration.exception.InstrumentNotFoundException;
import com.ravuri.calibration.exception.ValidationException;
import com.ravuri.calibration.repository.CalibrationInstrumentRepository;
import com.ravuri.calibration.repository.NonCalibrationInstrumentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UnifiedInstrumentService {

    private static final Logger LOGGER = LogManager.getLogger(UnifiedInstrumentService.class);

    @Autowired
    private CalibrationInstrumentRepository calibrationRepository;
    @Autowired
    private NonCalibrationInstrumentRepository nonCalibrationRepository;

    // Calibration Instrument Methods
    @Transactional
    public CalibrationInstrument createCalibrationInstrument(CalibrationInstrument instrument, String username) {
        try {
            validateCalibrationInstrument(instrument);
            if (calibrationRepository.existsBySerialNumber(instrument.getSerialNumber())) {
                throw new ValidationException("Calibration instrument with serial number " +
                        instrument.getSerialNumber() + " already exists");
            }

            setAuditFields(instrument, username);
            CalibrationInstrument savedInstrument = calibrationRepository.save(instrument);

            LOGGER.info("Created new calibration instrument: {} with serial number: {}",
                    savedInstrument.getName(), savedInstrument.getSerialNumber());
            return savedInstrument;
        } catch (Exception e) {
            LOGGER.error("Error creating calibration instrument", e);
            throw new ValidationException("Failed to create calibration instrument: " + e.getMessage());
        }
    }

    // Non-Calibration Instrument Methods
    @Transactional
    public NonCalibrationInstrument createNonCalibrationInstrument(NonCalibrationInstrument instrument, String username) {
        try {
            validateNonCalibrationInstrument(instrument);
            if (nonCalibrationRepository.existsBySerialNumber(instrument.getSerialNumber())) {
                throw new ValidationException("Non-calibration instrument with serial number " +
                        instrument.getSerialNumber() + " already exists");
            }

            setAuditFields(instrument, username);
            NonCalibrationInstrument savedInstrument = nonCalibrationRepository.save(instrument);

            LOGGER.info("Created new non-calibration instrument: {} with serial number: {}",
                    savedInstrument.getName(), savedInstrument.getSerialNumber());
            return savedInstrument;
        } catch (Exception e) {
            LOGGER.error("Error creating non-calibration instrument", e);
            throw new ValidationException("Failed to create non-calibration instrument: " + e.getMessage());
        }
    }

    // Retrieval Methods
    @Transactional(readOnly = true)
    public List<CalibrationInstrument> getAllCalibrationInstruments() {
        return calibrationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<NonCalibrationInstrument> getAllNonCalibrationInstruments() {
        return nonCalibrationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CalibrationInstrument getCalibrationInstrumentById(Long id) {
        return calibrationRepository.findById(id)
                .orElseThrow(() -> new InstrumentNotFoundException("Calibration instrument not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public NonCalibrationInstrument getNonCalibrationInstrumentById(Long id) {
        return nonCalibrationRepository.findById(id)
                .orElseThrow(() -> new InstrumentNotFoundException("Non-calibration instrument not found with id: " + id));
    }

    // Update Methods
    @Transactional
    public CalibrationInstrument updateCalibrationInstrument(
            Long id, CalibrationInstrument instrumentDetails, String username) {
        try {
            CalibrationInstrument instrument = getCalibrationInstrumentById(id);
            validateCalibrationInstrument(instrumentDetails);
            validateSerialNumberUpdate(instrument.getSerialNumber(),
                    instrumentDetails.getSerialNumber(), true);

            updateCalibrationInstrumentFields(instrument, instrumentDetails, username);
            CalibrationInstrument updatedInstrument = calibrationRepository.save(instrument);

            LOGGER.info("Updated calibration instrument: {} with serial number: {}",
                    updatedInstrument.getName(), updatedInstrument.getSerialNumber());
            return updatedInstrument;
        } catch (Exception e) {
            LOGGER.error("Error updating calibration instrument", e);
            throw new ValidationException("Failed to update calibration instrument: " + e.getMessage());
        }
    }

    @Transactional
    public NonCalibrationInstrument updateNonCalibrationInstrument(
            Long id, NonCalibrationInstrument instrumentDetails, String username) {
        try {
            NonCalibrationInstrument instrument = getNonCalibrationInstrumentById(id);
            validateNonCalibrationInstrument(instrumentDetails);
            validateSerialNumberUpdate(instrument.getSerialNumber(),
                    instrumentDetails.getSerialNumber(), false);

            updateNonCalibrationInstrumentFields(instrument, instrumentDetails, username);
            NonCalibrationInstrument updatedInstrument = nonCalibrationRepository.save(instrument);

            LOGGER.info("Updated non-calibration instrument: {} with serial number: {}",
                    updatedInstrument.getName(), updatedInstrument.getSerialNumber());
            return updatedInstrument;
        } catch (Exception e) {
            LOGGER.error("Error updating non-calibration instrument", e);
            throw new ValidationException("Failed to update non-calibration instrument: " + e.getMessage());
        }
    }

    // Status and Maintenance Methods
    @Transactional(readOnly = true)
    public List<CalibrationInstrument> getInstrumentsDueForCalibration() {
        return calibrationRepository.findByNextCalibrationDateBefore(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<NonCalibrationInstrument> getInstrumentsDueForMaintenance() {
        return nonCalibrationRepository.findByNextMaintenanceDateBefore(LocalDateTime.now());
    }

    // Private Helper Methods
    private void validateCalibrationInstrument(CalibrationInstrument instrument) {
        validateCommonFields(instrument.getName(), instrument.getSerialNumber(),
                instrument.getModel(), instrument.getManufacturer());

        if (instrument.getLastCalibrationDate() == null) {
            throw new ValidationException("Last calibration date is required");
        }
        if (instrument.getNextCalibrationDate() == null) {
            throw new ValidationException("Next calibration date is required");
        }
        if (instrument.getNextCalibrationDate().isBefore(instrument.getLastCalibrationDate())) {
            throw new ValidationException("Next calibration date must be after last calibration date");
        }
        if (instrument.getCalibrationIntervalDays() == null ||
                instrument.getCalibrationIntervalDays() <= 0) {
            throw new ValidationException("Calibration interval must be greater than 0 days");
        }
        if (instrument.getCalibrationAccuracy() == null ||
                instrument.getCalibrationAccuracy() <= 0) {
            throw new ValidationException("Calibration accuracy must be greater than 0");
        }
    }

    private void validateNonCalibrationInstrument(NonCalibrationInstrument instrument) {
        validateCommonFields(instrument.getName(), instrument.getSerialNumber(),
                instrument.getModel(), instrument.getManufacturer());

        if (instrument.getLastMaintenanceDate() == null) {
            throw new ValidationException("Last maintenance date is required");
        }
        if (instrument.getNextMaintenanceDate() == null) {
            throw new ValidationException("Next maintenance date is required");
        }
        if (instrument.getNextMaintenanceDate().isBefore(instrument.getLastMaintenanceDate())) {
            throw new ValidationException("Next maintenance date must be after last maintenance date");
        }
        if (instrument.getMaintenanceIntervalDays() == null ||
                instrument.getMaintenanceIntervalDays() <= 0) {
            throw new ValidationException("Maintenance interval must be greater than 0 days");
        }
    }

    private void validateCommonFields(String name, String serialNumber, String model, String manufacturer) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Name is required");
        }
        if (serialNumber == null || serialNumber.trim().isEmpty()) {
            throw new ValidationException("Serial number is required");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new ValidationException("Model is required");
        }
        if (manufacturer == null || manufacturer.trim().isEmpty()) {
            throw new ValidationException("Manufacturer is required");
        }
    }

    private void validateSerialNumberUpdate(String currentSerial, String newSerial, boolean isCalibration) {
        if (!currentSerial.equals(newSerial)) {
            boolean exists = isCalibration ?
                    calibrationRepository.existsBySerialNumber(newSerial) :
                    nonCalibrationRepository.existsBySerialNumber(newSerial);

            if (exists) {
                throw new ValidationException("Instrument with serial number " + newSerial + " already exists");
            }
        }
    }

    private void setAuditFields(Object instrument, String username) {
        LocalDateTime now = LocalDateTime.now();

        if (instrument instanceof CalibrationInstrument calibInstrument) {
            calibInstrument.setCreatedAt(now);
            calibInstrument.setCreatedBy(username);
            calibInstrument.setLastModifiedAt(now);
            calibInstrument.setLastModifiedBy(username);
        } else if (instrument instanceof NonCalibrationInstrument nonCalibInstrument) {
            nonCalibInstrument.setCreatedAt(now);
            nonCalibInstrument.setCreatedBy(username);
            nonCalibInstrument.setLastModifiedAt(now);
            nonCalibInstrument.setLastModifiedBy(username);
        }
    }

    private void updateCalibrationInstrumentFields(
            CalibrationInstrument instrument,
            CalibrationInstrument details,
            String username) {
        instrument.setName(details.getName());
        instrument.setModel(details.getModel());
        instrument.setManufacturer(details.getManufacturer());
        instrument.setSerialNumber(details.getSerialNumber());
        instrument.setLastCalibrationDate(details.getLastCalibrationDate());
        instrument.setNextCalibrationDate(details.getNextCalibrationDate());
        instrument.setCalibrationIntervalDays(details.getCalibrationIntervalDays());
        instrument.setCalibrationAccuracy(details.getCalibrationAccuracy());
        instrument.setCalibrationProcedure(details.getCalibrationProcedure());
        instrument.setStatus(details.getStatus());
        instrument.setLastModifiedAt(LocalDateTime.now());
        instrument.setLastModifiedBy(username);
    }

    private void updateNonCalibrationInstrumentFields(
            NonCalibrationInstrument instrument,
            NonCalibrationInstrument details,
            String username) {
        instrument.setName(details.getName());
        instrument.setModel(details.getModel());
        instrument.setManufacturer(details.getManufacturer());
        instrument.setSerialNumber(details.getSerialNumber());
        instrument.setLastMaintenanceDate(details.getLastMaintenanceDate());
        instrument.setNextMaintenanceDate(details.getNextMaintenanceDate());
        instrument.setMaintenanceIntervalDays(details.getMaintenanceIntervalDays());
        instrument.setMaintenanceProcedure(details.getMaintenanceProcedure());
        instrument.setStatus(details.getStatus());
        instrument.setLastModifiedAt(LocalDateTime.now());
        instrument.setLastModifiedBy(username);
    }

}
