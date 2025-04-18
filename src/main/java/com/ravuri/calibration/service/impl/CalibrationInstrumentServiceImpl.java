package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.CalibrationInstrument;
import com.ravuri.calibration.exception.InstrumentNotFoundException;
import com.ravuri.calibration.exception.ValidationException;
import com.ravuri.calibration.repository.CalibrationInstrumentRepository;
import com.ravuri.calibration.service.CalibrationInstrumentService;
import jakarta.persistence.Version;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public class CalibrationInstrumentServiceImpl implements CalibrationInstrumentService {

    private static final Logger LOGGER = LogManager.getLogger(CalibrationInstrumentServiceImpl.class);
    private CalibrationInstrumentRepository repository;

    @Override
    @Transactional
    public CalibrationInstrument createInstrument(CalibrationInstrument instrument, String username) {
        validateInstrument(instrument);
        if (repository.existsBySerialNumber(instrument.getSerialNumber())) {
            throw new ValidationException("Instrument with serial number " + instrument.getSerialNumber() + " already exists");
        }

        LocalDateTime now = LocalDateTime.now();
        instrument.setCreatedAt(now);
        instrument.setCreatedBy(username);
        instrument.setLastModifiedAt(now);
        instrument.setLastModifiedBy(username);

        CalibrationInstrument savedInstrument = repository.save(instrument);
        LOGGER.info("Created new calibration instrument: {} with serial number: {}",
                savedInstrument.getName(), savedInstrument.getSerialNumber());
        return savedInstrument;
    }

    @Override
    @Transactional
    public CalibrationInstrument updateInstrument(Long id, CalibrationInstrument instrumentDetails, String username) {
        CalibrationInstrument instrument = getInstrumentById(id);
        validateInstrument(instrumentDetails);

        if (!instrument.getSerialNumber().equals(instrumentDetails.getSerialNumber()) &&
                repository.existsBySerialNumber(instrumentDetails.getSerialNumber())) {
            throw new ValidationException("Instrument with serial number " + instrumentDetails.getSerialNumber() + " already exists");
        }

        updateInstrumentFields(instrument, instrumentDetails, username);
        CalibrationInstrument updatedInstrument = repository.save(instrument);
        LOGGER.info("Updated calibration instrument: {} with serial number: {}",
                updatedInstrument.getName(), updatedInstrument.getSerialNumber());
        return updatedInstrument;
    }

    @Override
    @Transactional(readOnly = true)
    public CalibrationInstrument getInstrumentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new InstrumentNotFoundException("Calibration instrument not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalibrationInstrument> getAllInstruments() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalibrationInstrument> getInstrumentsDueForCalibration() {
        return repository.findByNextCalibrationDateBefore(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalibrationInstrument> getInstrumentsByStatus(CalibrationInstrument.InstrumentStatus status) {
        return repository.findByStatus(status);
    }

    private void validateInstrument(CalibrationInstrument instrument) {
        if (instrument.getLastCalibrationDate() == null) {
            throw new ValidationException("Last calibration date is required");
        }
        if (instrument.getNextCalibrationDate() == null) {
            throw new ValidationException("Next calibration date is required");
        }
        if (instrument.getNextCalibrationDate().isBefore(instrument.getLastCalibrationDate())) {
            throw new ValidationException("Next calibration date must be after last calibration date");
        }
        if (instrument.getCalibrationIntervalDays() == null || instrument.getCalibrationIntervalDays() <= 0) {
            throw new ValidationException("Calibration interval must be greater than 0 days");
        }
        if (instrument.getCalibrationAccuracy() == null || instrument.getCalibrationAccuracy() <= 0) {
            throw new ValidationException("Calibration accuracy must be greater than 0");
        }
        if (instrument.getStatus() == null) {
            throw new ValidationException("Instrument status is required");
        }
    }

    private void updateInstrumentFields(CalibrationInstrument instrument, CalibrationInstrument details, String username) {
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

}
