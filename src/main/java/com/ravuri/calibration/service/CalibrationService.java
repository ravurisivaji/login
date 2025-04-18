package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.CalibrationRecord;

import java.util.List;

public interface CalibrationService {

    public CalibrationRecord calibrateInstrument(
            String instrumentId,
            String portName,
            Double standardPhValue,
            String username);
    public List<CalibrationRecord> getCalibrationHistory(String instrumentId);
    public List<CalibrationRecord> getDueCalibrations();
}
