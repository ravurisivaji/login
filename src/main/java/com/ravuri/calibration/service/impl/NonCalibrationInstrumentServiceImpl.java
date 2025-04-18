package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.NonCalibrationInstrument;
import com.ravuri.calibration.exception.InstrumentNotFoundException;
import com.ravuri.calibration.exception.ValidationException;
import com.ravuri.calibration.repository.NonCalibrationInstrumentRepository;
import com.ravuri.calibration.service.NonCalibrationInstrumentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NonCalibrationInstrumentServiceImpl implements NonCalibrationInstrumentService {

    private static final Logger LOGGER = LogManager.getLogger(NonCalibrationInstrumentServiceImpl.class);

    @Autowired
    private NonCalibrationInstrumentRepository repository;

    @Transactional
    public NonCalibrationInstrument createInstrument(NonCalibrationInstrument instrument, String username) {
        validateInstrument(instrument);
        if (repository.existsBySerialNumber(instrument.getSerialNumber())) {
            throw new ValidationException("Instrument with serial number " + instrument.getSerialNumber() + " already exists");
        }

        LocalDateTime now = LocalDateTime.now();
        instrument.setCreatedAt(now);
        instrument.setCreatedBy(username);
        instrument.setLastModifiedAt(now);
        instrument.setLastModifiedBy(username);

        NonCalibrationInstrument savedInstrument = repository.save(instrument);
        LOGGER.info("Created new non-calibration instrument: {} with serial number: {}",
                savedInstrument.getName(), savedInstrument.getSerialNumber());
        return savedInstrument;
    }

    @Transactional
    public NonCalibrationInstrument updateInstrument(Long id, NonCalibrationInstrument instrumentDetails, String username) {
        NonCalibrationInstrument instrument = getInstrumentById(id);
        validateInstrument(instrumentDetails);

        if (!instrument.getSerialNumber().equals(instrumentDetails.getSerialNumber()) &&
                repository.existsBySerialNumber(instrumentDetails.getSerialNumber())) {
            throw new ValidationException("Instrument with serial number " + instrumentDetails.getSerialNumber() + " already exists");
        }

        updateInstrumentFields(instrument, instrumentDetails, username);
        NonCalibrationInstrument updatedInstrument = repository.save(instrument);
        LOGGER.info("Updated non-calibration instrument: {} with serial number: {}",
                updatedInstrument.getName(), updatedInstrument.getSerialNumber());
        return updatedInstrument;
    }

    @Transactional(readOnly = true)
    public NonCalibrationInstrument getInstrumentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new InstrumentNotFoundException("Non-calibration instrument not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<NonCalibrationInstrument> getAllInstruments() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<NonCalibrationInstrument> getInstrumentsDueForMaintenance() {
        return repository.findByNextMaintenanceDateBefore(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<NonCalibrationInstrument> getInstrumentsByStatus(NonCalibrationInstrument.InstrumentStatus status) {
        return repository.findByStatus(status);
    }

    private void validateInstrument(NonCalibrationInstrument instrument) {
        if (instrument.getLastMaintenanceDate() == null) {
            throw new ValidationException("Last maintenance date is required");
        }
        if (instrument.getNextMaintenanceDate() == null) {
            throw new ValidationException("Next maintenance date is required");
        }
        if (instrument.getNextMaintenanceDate().isBefore(instrument.getLastMaintenanceDate())) {
            throw new ValidationException("Next maintenance date must be after last maintenance date");
        }
        if (instrument.getMaintenanceIntervalDays() == null || instrument.getMaintenanceIntervalDays() <= 0) {
            throw new ValidationException("Maintenance interval must be greater than 0 days");
        }
        if (instrument.getStatus() == null) {
            throw new ValidationException("Instrument status is required");
        }
    }

    private void updateInstrumentFields(NonCalibrationInstrument instrument, NonCalibrationInstrument details, String username) {
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
