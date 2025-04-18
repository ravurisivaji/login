package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.InstrumentLock;
import com.ravuri.calibration.entity.LockStatus;
import com.ravuri.calibration.exception.InstrumentConnectionException;
import com.ravuri.calibration.exception.InstrumentException;
import com.ravuri.calibration.repository.InstrumentLockRepository;
import com.ravuri.calibration.service.InstrumentLockService;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InstrumentLockServiceImpl implements InstrumentLockService {

    private static final Logger LOGGER = LogManager.getLogger(InstrumentLockServiceImpl.class);

    @Autowired
    private InstrumentLockRepository lockRepository;

    @Override
    @Transactional
    public InstrumentLock lockInstrument(String instrumentId, String userId, String purpose, String portName) {
        try {
            validateLockRequest(instrumentId, userId, purpose);
            checkForExistingLock(instrumentId);
            verifyInstrumentConnection(portName);

            InstrumentLock lock = new InstrumentLock();
            lock.setInstrumentId(instrumentId);
            lock.setUserId(userId);
            lock.setPurpose(purpose);
            lock.setStartTime(LocalDateTime.now());
            lock.setStatus(LockStatus.ACTIVE);

            InstrumentLock savedLock = lockRepository.save(lock);
            LOGGER.info("Instrument {} locked by user {} for {}", instrumentId, userId, purpose);
            return savedLock;

        } catch (Exception e) {
            LOGGER.error("Error locking instrument {}", instrumentId, e);
            throw new InstrumentException("Failed to lock instrument: " + e.getMessage(), e);
        }
    }

    @Transactional
    public InstrumentLock releaseLock(String instrumentId, String userId) {
        try {
            InstrumentLock lock = lockRepository.findActiveLock(instrumentId)
                    .orElseThrow(() -> new InstrumentException("No active lock found for instrument: " + instrumentId));

            validateLockOwnership(lock, userId);

            lock.setEndTime(LocalDateTime.now());
            lock.setStatus(LockStatus.RELEASED);

            InstrumentLock updatedLock = lockRepository.save(lock);
            LOGGER.info("Lock released for instrument {} by user {}", instrumentId, userId);
            return updatedLock;

        } catch (Exception e) {
            LOGGER.error("Error releasing lock for instrument {}", instrumentId, e);
            throw new InstrumentException("Failed to release lock: " + e.getMessage(), e);
        }
    }

    @Transactional
    public InstrumentLock forceReleaseLock(String instrumentId, String adminUserId) {
        try {
            InstrumentLock lock = lockRepository.findActiveLock(instrumentId)
                    .orElseThrow(() -> new InstrumentException("No active lock found for instrument: " + instrumentId));

            lock.setEndTime(LocalDateTime.now());
            lock.setStatus(LockStatus.FORCE_RELEASED);

            InstrumentLock updatedLock = lockRepository.save(lock);
            LOGGER.warn("Lock force released for instrument {} by admin {}", instrumentId, adminUserId);
            return updatedLock;

        } catch (Exception e) {
            LOGGER.error("Error force releasing lock for instrument {}", instrumentId, e);
            throw new InstrumentException("Failed to force release lock: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public boolean isInstrumentLocked(String instrumentId) {
        return lockRepository.findActiveLock(instrumentId).isPresent();
    }

    @Transactional(readOnly = true)
    public InstrumentLock getActiveLock(String instrumentId) {
        return lockRepository.findActiveLock(instrumentId)
                .orElseThrow(() -> new InstrumentException("No active lock found for instrument: " + instrumentId));
    }

    private void validateLockRequest(String instrumentId, String userId, String purpose) {
        if (instrumentId == null || instrumentId.trim().isEmpty()) {
            throw new InstrumentException("Instrument ID is required");
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new InstrumentException("User ID is required");
        }
        if (purpose == null || purpose.trim().isEmpty()) {
            throw new InstrumentException("Purpose is required");
        }
    }

    private void checkForExistingLock(String instrumentId) {
        Optional<InstrumentLock> existingLock = lockRepository.findActiveLock(instrumentId);
        if (existingLock.isPresent()) {
            InstrumentLock lock = existingLock.get();
            throw new InstrumentException(
                    String.format("Instrument %s is already locked by user %s since %s",
                            instrumentId, lock.getUserId(), lock.getStartTime()));
        }
    }

    private void validateLockOwnership(InstrumentLock lock, String userId) {
        if (!lock.getUserId().equals(userId)) {
            throw new InstrumentException(
                    String.format("Lock can only be released by the owner. Current owner: %s", lock.getUserId()));
        }
    }

    private void verifyInstrumentConnection(String portName) {
        SerialPort serialPort = new SerialPort(portName);
        try {
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE
            );
            serialPort.closePort();
        } catch (SerialPortException e) {
            throw new InstrumentConnectionException("Failed to verify instrument connection: " + e.getMessage(), e);
        }
    }
}
