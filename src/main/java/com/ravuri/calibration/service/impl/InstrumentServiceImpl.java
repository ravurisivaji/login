package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.Instrument;
import com.ravuri.calibration.entity.InstrumentStatus;
import com.ravuri.calibration.exception.DuplicateInstrumentException;
import com.ravuri.calibration.exception.InstrumentNotFoundException;
import com.ravuri.calibration.exception.ValidationException;
import com.ravuri.calibration.repository.InstrumentRepository;
import com.ravuri.calibration.service.InstrumentService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class InstrumentServiceImpl implements InstrumentService {

    private static final Logger LOGGER = LogManager.getLogger(InstrumentServiceImpl.class);

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Instrument> getAllInstruments() {
        LOGGER.info("Fetching all instruments");
        return instrumentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Instrument getInstrumentById(Long id) {
        LOGGER.info("Fetching instrument with ID: {}", id);
        return instrumentRepository.findById(id)
                .orElseThrow(() -> new InstrumentNotFoundException("Instrument not found with id: " + id));

    }

    @Override
    @Transactional()
    public Instrument createInstrument(Instrument instrument) {
        LOGGER.info("Creating new instrument: {}", instrument);

        validateInstrument(instrument);
        if (instrumentRepository.existsBySerialNumber(instrument.getSerialNumber())) {
            throw new DuplicateInstrumentException("Instrument with serial number " + instrument.getSerialNumber() + " already exists");
        }
        if (instrumentRepository.existsBySerialNumber(instrument.getSerialNumber())) {
            throw new RuntimeException("Instrument with serial number " + instrument.getSerialNumber() + " already exists");
        }
        return instrumentRepository.save(instrument);
    }

    @Override
    @Transactional()
    public Instrument updateInstrument(Long id, Instrument instrumentDetails) {
        LOGGER.info("Updating instrument with ID: {} with details: {}", id, instrumentDetails);

        Instrument instrument = getInstrumentById(id);
        validateInstrument(instrumentDetails);

        if (!instrument.getSerialNumber().equals(instrumentDetails.getSerialNumber()) &&
                instrumentRepository.existsBySerialNumber(instrumentDetails.getSerialNumber())) {
            throw new DuplicateInstrumentException("Instrument with serial number " + instrumentDetails.getSerialNumber() + " already exists");
        }

        instrument.setName(instrumentDetails.getName());
        instrument.setManufacturer(instrumentDetails.getManufacturer());
        instrument.setModel(instrumentDetails.getModel());
        instrument.setSerialNumber(instrumentDetails.getSerialNumber());
        instrument.setCalibrationDate(instrumentDetails.getCalibrationDate());
        instrument.setNextCalibrationDate(instrumentDetails.getNextCalibrationDate());
        instrument.setDescription(instrumentDetails.getDescription());
        instrument.setStatus(instrumentDetails.getStatus());

        return instrumentRepository.save(instrument);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Instrument> getInstrumentsDueForCalibration() {
        LOGGER.info("Fetching instruments due for calibration");
        return instrumentRepository.findByNextCalibrationDateBefore(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Instrument> getInstrumentsByStatus(InstrumentStatus status) {
        LOGGER.info("Fetching instruments with status: {}", status);
        if (status == null) {
            throw new ValidationException("Instrument status cannot be null");
        }

        return instrumentRepository.findByStatus(status);
    }

    private void validateInstrument(Instrument instrument) {
        if (instrument.getCalibrationDate() == null) {
            throw new ValidationException("Calibration date is required");
        }

        if (instrument.getNextCalibrationDate() == null) {
            throw new ValidationException("Next calibration date is required");
        }

        if (instrument.getNextCalibrationDate().isBefore(instrument.getCalibrationDate())) {
            throw new ValidationException("Next calibration date must be after calibration date");
        }

        if (instrument.getStatus() == null) {
            throw new ValidationException("Instrument status is required");
        }
    }
}
