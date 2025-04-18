package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.PHBufferReading;
import com.ravuri.calibration.exception.InstrumentConnectionException;
import com.ravuri.calibration.exception.InstrumentException;
import com.ravuri.calibration.exception.InvalidInstrumentDataException;
import com.ravuri.calibration.repository.PHBufferReadingRepository;
import com.ravuri.calibration.service.PHBufferService;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PHBufferServiceImpl implements PHBufferService {

    private static final Logger LOGGER = LogManager.getLogger(PHBufferServiceImpl.class);

    @Autowired
    private PHBufferReadingRepository repository;

    @Override
    @Transactional
    public PHBufferReading readAndSaveData(String instrumentId, String portName, String username) {
        validateInput(instrumentId, portName, username);
        SerialPort serialPort = new SerialPort(portName);
        LocalDateTime now = LocalDateTime.now();

        try {
            connectToInstrument(serialPort);
            String response = readFromInstrument(serialPort, instrumentId);
            PHBufferReading reading = parseAndValidateResponse(response, instrumentId);

            // Set audit fields
            reading.setReadBy(username);
            reading.setReadAt(now);
            reading.setCreatedAt(now);
            reading.setCreatedBy(username);
            reading.setLastModifiedAt(now);
            reading.setLastModifiedBy(username);

            PHBufferReading savedReading = repository.save(reading);
            LOGGER.info("Successfully saved reading from instrument {} by user {}: pH={}, temp={}",
                    instrumentId, username, reading.getPhValue(), reading.getTemperature());

            return savedReading;

        } catch (SerialPortException e) {
            LOGGER.error("Serial port error for instrument {}: {}", instrumentId, e.getMessage());
            throw new InstrumentConnectionException("Failed to communicate with instrument: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error("Error reading instrument {}: {}", instrumentId, e.getMessage());
            throw new InstrumentException("Error reading instrument data: " + e.getMessage(), e);
        } finally {
            closePort(serialPort);
        }
    }

    private void validateInput(String instrumentId, String portName, String username) {
        if (instrumentId == null || instrumentId.trim().isEmpty()) {
            throw new InvalidInstrumentDataException("Instrument ID cannot be empty");
        }
        if (portName == null || portName.trim().isEmpty()) {
            throw new InvalidInstrumentDataException("Port name cannot be empty");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidInstrumentDataException("Username cannot be empty");
        }
    }

    private void connectToInstrument(SerialPort serialPort) throws SerialPortException {
        try {
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE
            );
        } catch (SerialPortException e) {
            throw new InstrumentConnectionException("Failed to connect to instrument: " + e.getMessage(), e);
        }
    }

    private String readFromInstrument(SerialPort serialPort, String instrumentId) throws SerialPortException {
        try {
            serialPort.writeBytes("READ\r\n".getBytes());
            Thread.sleep(1000); // Wait for response

            String response = serialPort.readString();
            if (response == null || response.trim().isEmpty()) {
                throw new InstrumentException("No response from instrument " + instrumentId);
            }
            return response;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InstrumentException("Reading interrupted for instrument " + instrumentId);
        }
    }

    private PHBufferReading parseAndValidateResponse(String response, String instrumentId) {
        try {
            PHBufferReading reading = new PHBufferReading();
            reading.setInstrumentId(instrumentId);
            reading.setReadingTime(LocalDateTime.now());

            String[] parts = response.split(";");
            boolean hasValidData = false;

            for (String part : parts) {
                String[] keyValue = part.split(":");
                if (keyValue.length != 2) continue;

                switch (keyValue[0].trim().toUpperCase()) {
                    case "PH":
                        reading.setPhValue(parseDouble(keyValue[1], "pH"));
                        hasValidData = true;
                        break;
                    case "TEMP":
                        reading.setTemperature(parseDouble(keyValue[1], "temperature"));
                        break;
                    case "COND":
                        reading.setConductivity(parseDouble(keyValue[1], "conductivity"));
                        break;
                }
            }

            if (!hasValidData) {
                throw new InvalidInstrumentDataException("No valid pH reading found in response");
            }

            validateReading(reading);
            reading.setStatus("OK");
            return reading;

        } catch (NumberFormatException e) {
            throw new InvalidInstrumentDataException("Invalid numeric value in instrument response: " + e.getMessage());
        }
    }

    private Double parseDouble(String value, String field) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new InvalidInstrumentDataException("Invalid " + field + " value: " + value);
        }
    }

    private void validateReading(PHBufferReading reading) {
        if (reading.getPhValue() == null || reading.getPhValue() < 0 || reading.getPhValue() > 14) {
            throw new InvalidInstrumentDataException("pH value out of range (0-14): " + reading.getPhValue());
        }
        if (reading.getTemperature() != null && (reading.getTemperature() < -10 || reading.getTemperature() > 100)) {
            throw new InvalidInstrumentDataException("Temperature out of range (-10°C to 100°C): " + reading.getTemperature());
        }
        if (reading.getConductivity() != null && reading.getConductivity() < 0) {
            throw new InvalidInstrumentDataException("Negative conductivity value: " + reading.getConductivity());
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

    @Override
    @Transactional(readOnly = true)
    public List<PHBufferReading> getReadingsByInstrument(String instrumentId) {
        if (instrumentId == null || instrumentId.trim().isEmpty()) {
            throw new InvalidInstrumentDataException("Instrument ID cannot be empty");
        }
        return repository.findByInstrumentId(instrumentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PHBufferReading> getReadingsByTimeRange(
            String instrumentId, LocalDateTime startTime, LocalDateTime endTime) {
        validateTimeRange(instrumentId, startTime, endTime);
        return repository.findByInstrumentIdAndReadingTimeBetween(instrumentId, startTime, endTime);
    }

    private void validateTimeRange(String instrumentId, LocalDateTime startTime, LocalDateTime endTime) {
        if (instrumentId == null || instrumentId.trim().isEmpty()) {
            throw new InvalidInstrumentDataException("Instrument ID cannot be empty");
        }
        if (startTime == null || endTime == null) {
            throw new InvalidInstrumentDataException("Start time and end time cannot be null");
        }
        if (endTime.isBefore(startTime)) {
            throw new InvalidInstrumentDataException("End time cannot be before start time");
        }
    }

}
