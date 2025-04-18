package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.InstrumentLock;

public interface InstrumentLockService {
    public InstrumentLock lockInstrument(String instrumentId, String userId, String purpose, String portName);
    public InstrumentLock releaseLock(String instrumentId, String userId);
    public InstrumentLock forceReleaseLock(String instrumentId, String adminUserId);
    public boolean isInstrumentLocked(String instrumentId);
    public InstrumentLock getActiveLock(String instrumentId);
}
