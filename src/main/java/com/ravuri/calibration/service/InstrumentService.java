package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.Instrument;
import com.ravuri.calibration.entity.InstrumentStatus;

import java.util.List;

public interface InstrumentService {

    public List<Instrument> getAllInstruments();
    public Instrument getInstrumentById(Long id);
    public Instrument createInstrument(Instrument instrument);
    public Instrument updateInstrument(Long id, Instrument instrumentDetails);
    public List<Instrument> getInstrumentsDueForCalibration();
    public List<Instrument> getInstrumentsByStatus(InstrumentStatus status);
}
