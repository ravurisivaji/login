package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.PHBufferReading;

import java.time.LocalDateTime;
import java.util.List;

public interface PHBufferService {

    public PHBufferReading readAndSaveData(String instrumentId, String portName, String username);
    public List<PHBufferReading> getReadingsByInstrument(String instrumentId);
    public List<PHBufferReading> getReadingsByTimeRange(
            String instrumentId, LocalDateTime startTime, LocalDateTime endTime);
}
