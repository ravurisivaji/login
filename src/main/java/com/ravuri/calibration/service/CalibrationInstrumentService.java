package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.CalibrationInstrument;

import java.util.List;

public interface CalibrationInstrumentService {
    public CalibrationInstrument createInstrument(CalibrationInstrument instrument, String username);
    public CalibrationInstrument updateInstrument(Long id, CalibrationInstrument instrumentDetails, String username);
    public CalibrationInstrument getInstrumentById(Long id);
    public List<CalibrationInstrument> getAllInstruments();
    public List<CalibrationInstrument> getInstrumentsDueForCalibration();
    public List<CalibrationInstrument> getInstrumentsByStatus(CalibrationInstrument.InstrumentStatus status);
}
