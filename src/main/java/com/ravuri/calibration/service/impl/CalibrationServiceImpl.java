package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.constant.CalibrationConstants;
import com.ravuri.calibration.entity.CalibrationRecord;
import com.ravuri.calibration.entity.CalibrationStatus;
import com.ravuri.calibration.exception.*;
import com.ravuri.calibration.repository.CalibrationRepository;
import com.ravuri.calibration.service.CalibrationService;
import com.ravuri.calibration.service.PHBufferService;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CalibrationServiceImpl implements CalibrationService {

    private static final Logger LOGGER = LogManager.getLogger(CalibrationServiceImpl.class);

    private CalibrationRepository calibrationRepository;
    private PHBufferService phBufferService;


    @Transactional
    public CalibrationRecord calibrateInstrument(
            String instrumentId,
            String portName,
            Double standardPhValue,
            String username) {

        try {
            validateCalibrationInput(instrumentId, standardPhValue, username);
            SerialPort serialPort = initializeSerialPort(portName);
            LocalDateTime now = LocalDateTime.now();

            try {
                double measuredPhValue = readCurrentPhValue(serialPort, instrumentId);
                CalibrationParameters params = calculateCalibrationParameters(standardPhValue, measuredPhValue);
                validateCalibrationResults(params);

                CalibrationRecord calibration = createCalibrationRecord(
                        instrumentId, standardPhValue, measuredPhValue, params, username, now);

                CalibrationRecord savedCalibration = calibrationRepository.save(calibration);
                LOGGER.info("Calibration completed for instrument {}: status={}, slope={}, offset={}",
                        instrumentId, savedCalibration.getStatus(), params.getSlope(), params.getOffset());

                return savedCalibration;

            } finally {
                closePort(serialPort);
            }
        } catch (SerialPortException e) {
            LOGGER.error("Serial port error during calibration for instrument {}: {}", instrumentId, e.getMessage());
            throw new InstrumentConnectionException("Failed to communicate with instrument: " + e.getMessage(), e);
        } catch (CalibrationValidationException | InvalidInstrumentDataException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unexpected error during calibration for instrument {}: {}", instrumentId, e.getMessage());
            throw new CalibrationException("Calibration failed: " + e.getMessage(), e);
        }
    }

    private SerialPort initializeSerialPort(String portName) throws SerialPortException {
        SerialPort serialPort = new SerialPort(portName);
        try {
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE
            );
            return serialPort;
        } catch (SerialPortException e) {
            throw new InstrumentConnectionException("Failed to initialize serial port: " + e.getMessage(), e);
        }
    }

    private double readCurrentPhValue(SerialPort serialPort, String instrumentId) throws SerialPortException {
        try {
            serialPort.writeBytes("CAL\r\n".getBytes());
            Thread.sleep(CalibrationConstants.CALIBRATION_WAIT_TIME);

            String response = serialPort.readString();
            if (response == null || response.trim().isEmpty()) {
                throw new InstrumentException("No response from instrument during calibration");
            }

            return parsePhValue(response);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CalibrationException("Calibration interrupted");
        }
    }

    private double parsePhValue(String response) {
        try {
            String[] parts = response.split(";");
            for (String part : parts) {
                String[] keyValue = part.split(":");
                if (keyValue.length == 2 && keyValue[0].trim().equalsIgnoreCase("PH")) {
                    double value = Double.parseDouble(keyValue[1].trim());
                    validatePhValue(value);
                    return value;
                }
            }
            throw new InvalidInstrumentDataException("pH value not found in response");
        } catch (NumberFormatException e) {
            throw new InvalidInstrumentDataException("Invalid pH value format in response");
        }
    }

    private void validatePhValue(double value) {
        if (value < 0 || value > 14) {
            throw new InvalidInstrumentDataException("pH value out of valid range (0-14): " + value);
        }
    }

    private CalibrationParameters calculateCalibrationParameters(double standardPh, double measuredPh) {
        double slope = Math.abs(standardPh - measuredPh);
        double offset = standardPh - measuredPh;
        return new CalibrationParameters(slope, offset);
    }

    private void validateCalibrationInput(String instrumentId, Double standardPhValue, String username) {
        if (instrumentId == null || instrumentId.trim().isEmpty()) {
            throw new CalibrationValidationException("Instrument ID cannot be empty");
        }
        if (standardPhValue == null || standardPhValue < 0 || standardPhValue > 14) {
            throw new CalibrationValidationException("Invalid standard pH value: " + standardPhValue);
        }
        if (username == null || username.trim().isEmpty()) {
            throw new CalibrationValidationException("Username cannot be empty");
        }
    }

    private void validateCalibrationResults(CalibrationParameters params) {
        if (params.getSlope() < CalibrationConstants.MIN_ACCEPTABLE_SLOPE || params.getSlope() > CalibrationConstants.MAX_ACCEPTABLE_SLOPE) {
            throw new CalibrationValidationException(
                    String.format("Calibration slope %.2f is outside acceptable range (%.2f - %.2f)",
                            params.getSlope(), CalibrationConstants.MIN_ACCEPTABLE_SLOPE, CalibrationConstants.MAX_ACCEPTABLE_SLOPE));
        }
        if (Math.abs(params.getOffset()) > CalibrationConstants.MAX_ACCEPTABLE_OFFSET) {
            throw new CalibrationValidationException(
                    String.format("Calibration offset %.2f exceeds maximum allowed value (±%.2f)",
                            params.getOffset(), CalibrationConstants.MAX_ACCEPTABLE_OFFSET));
        }
    }

    private CalibrationRecord createCalibrationRecord(
            String instrumentId,
            double standardPhValue,
            double measuredPhValue,
            CalibrationParameters params,
            String username,
            LocalDateTime now) {

        CalibrationRecord calibration = new CalibrationRecord();
        calibration.setInstrumentId(instrumentId);
        calibration.setStandardPhValue(standardPhValue);
        calibration.setMeasuredPhValue(measuredPhValue);
        calibration.setTemperature(25.0); // Default temperature
        calibration.setSlope(params.getSlope());
        calibration.setOffset(params.getOffset());
        calibration.setStatus(determineCalibrationStatus(params));
        calibration.setCalibrationTime(now);
        calibration.setCalibratedBy(username);
        calibration.setNextCalibrationDue(now.plusMonths(1));
        return calibration;
    }

    private CalibrationStatus determineCalibrationStatus(CalibrationParameters params) {
        if (params.getSlope() >= 0.95 && params.getSlope() <= 1.05 && Math.abs(params.getOffset()) <= 0.5) {
            return CalibrationStatus.SUCCESSFUL;
        } else if (params.getSlope() >= 0.9 && params.getSlope() <= 1.1 && Math.abs(params.getOffset()) <= 1.0) {
            return CalibrationStatus.NEEDS_VERIFICATION;
        } else {
            return CalibrationStatus.FAILED;
        }
    }

    private void closePort(SerialPort serialPort) {
        try {
            if (serialPort != null && serialPort.isOpened()) {
                serialPort.closePort();
            }
        } catch (SerialPortException e) {
            LOGGER.warn("Error closing serial port: {}", e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<CalibrationRecord> getCalibrationHistory(String instrumentId) {
        validateInstrumentId(instrumentId);
        return calibrationRepository.findByInstrumentId(instrumentId);
    }

    @Transactional(readOnly = true)
    public List<CalibrationRecord> getDueCalibrations() {
        return calibrationRepository.findByNextCalibrationDueBefore(LocalDateTime.now());
    }

    private void validateInstrumentId(String instrumentId) {
        if (instrumentId == null || instrumentId.trim().isEmpty()) {
            throw new CalibrationValidationException("Instrument ID cannot be empty");
        }
    }

    private static class CalibrationParameters {
        private final double slope;
        private final double offset;

        public CalibrationParameters(double slope, double offset) {
            this.slope = slope;
            this.offset = offset;
        }

        public double getSlope() {
            return slope;
        }

        public double getOffset() {
            return offset;
        }
    }

}
