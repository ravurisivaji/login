package com.ravuri.calibration.controller;

import com.ravuri.calibration.entity.Instrument;
import com.ravuri.calibration.entity.InstrumentStatus;
import com.ravuri.calibration.service.InstrumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/instruments")
public class InstrumentController {

    private static final Logger LOGGER = LogManager.getLogger(InstrumentController.class);

    @Autowired
    private InstrumentService instrumentService;

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Instrument>> getAllInstruments() {
        return ResponseEntity.ok(instrumentService.getAllInstruments());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Instrument> getInstrumentById(@PathVariable Long id) {
        return ResponseEntity.ok(instrumentService.getInstrumentById(id));
    }

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Instrument> createInstrument(@Valid @RequestBody Instrument instrument) {
        return ResponseEntity.ok(instrumentService.createInstrument(instrument));
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Instrument> updateInstrument(
            @PathVariable Long id,
            @Valid @RequestBody Instrument instrument) {
        return ResponseEntity.ok(instrumentService.updateInstrument(id, instrument));
    }

    @GetMapping(value = "/calibration-due", produces = "application/json")
    public ResponseEntity<List<Instrument>> getInstrumentsDueForCalibration() {
        return ResponseEntity.ok(instrumentService.getInstrumentsDueForCalibration());
    }

    @GetMapping(value = "/status/{status}", produces = "application/json")
    public ResponseEntity<List<Instrument>> getInstrumentsByStatus(
            @PathVariable InstrumentStatus status) {
        return ResponseEntity.ok(instrumentService.getInstrumentsByStatus(status));
    }
}
