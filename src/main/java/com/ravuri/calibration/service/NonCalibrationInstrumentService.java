package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.NonCalibrationInstrument;

import java.util.List;

public interface NonCalibrationInstrumentService {
    public NonCalibrationInstrument createInstrument(NonCalibrationInstrument instrument, String username);
    public NonCalibrationInstrument updateInstrument(Long id, NonCalibrationInstrument instrumentDetails, String username);
    public NonCalibrationInstrument getInstrumentById(Long id);
    public List<NonCalibrationInstrument> getInstrumentsDueForMaintenance();
    public List<NonCalibrationInstrument> getInstrumentsByStatus(NonCalibrationInstrument.InstrumentStatus status);
}
