package com.ravuri.calibration.controller;


import com.ravuri.calibration.entity.CalibrationRecord;
import com.ravuri.calibration.service.CalibrationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calibration")
public class CalibrationController {

    private static final Logger LOGGER = LogManager.getLogger(CalibrationController.class);

    @Autowired
    private CalibrationService calibrationService;


    @PostMapping("/{instrumentId}")
    public ResponseEntity<CalibrationRecord> calibrateInstrument(
            @PathVariable String instrumentId,
            @RequestParam String portName,
            @RequestParam Double standardPhValue,
            @RequestHeader("X-User-Name") String username) {
        LOGGER.info("Starting calibration for instrument {} with standard pH {} by user {}",
                instrumentId, standardPhValue, username);
        return ResponseEntity.ok(calibrationService.calibrateInstrument(
                instrumentId, portName, standardPhValue, username));
    }

    @GetMapping("/{instrumentId}/history")
    public ResponseEntity<List<CalibrationRecord>> getCalibrationHistory(
            @PathVariable String instrumentId) {
        LOGGER.info("Fetching calibration history for instrument {}", instrumentId);
        return ResponseEntity.ok(calibrationService.getCalibrationHistory(instrumentId));
    }

    @GetMapping("/due")
    public ResponseEntity<List<CalibrationRecord>> getDueCalibrations() {
        LOGGER.info("Fetching due calibrations");
        return ResponseEntity.ok(calibrationService.getDueCalibrations());
    }

}
