package com.ravuri.calibration.controller;

import com.ravuri.calibration.entity.PHBufferReading;
import com.ravuri.calibration.service.PHBufferService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/v1/phbuffer")
@RequiredArgsConstructor
public class PHBufferController {

    private static final Logger LOGGER = LogManager.getLogger(PHBufferController.class);
    @Autowired
    private PHBufferService phBufferService;

    @PostMapping("/{instrumentId}/read")
    public ResponseEntity<PHBufferReading> readInstrument(
            @PathVariable String instrumentId,
            @RequestParam String portName,
            @RequestHeader("X-User-Name") String username) {
        LOGGER.info("Reading data from instrument {} on port {} by user {}",
                instrumentId, portName, username);
        return ResponseEntity.ok(phBufferService.readAndSaveData(instrumentId, portName, username));
    }

    @GetMapping("/{instrumentId}")
    public ResponseEntity<List<PHBufferReading>> getReadings(@PathVariable String instrumentId) {
        LOGGER.info("Fetching readings for instrument {}", instrumentId);
        return ResponseEntity.ok(phBufferService.getReadingsByInstrument(instrumentId));
    }

    @GetMapping("/{instrumentId}/range")
    public ResponseEntity<List<PHBufferReading>> getReadingsByTimeRange(
            @PathVariable String instrumentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        LOGGER.info("Fetching readings for instrument {} between {} and {}",
                instrumentId, startTime, endTime);
        return ResponseEntity.ok(phBufferService.getReadingsByTimeRange(instrumentId, startTime, endTime));
    }

}
