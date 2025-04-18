package com.ravuri.calibration.controller;


import com.ravuri.calibration.entity.InstrumentUsage;
import com.ravuri.calibration.service.InstrumentUsageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/instrument-usage")
public class InstrumentUsageController {

    private static final Logger LOGGER = LogManager.getLogger(InstrumentUsageController.class);


    @Autowired
    private InstrumentUsageService usageService;

    @PostMapping(value = "/start", consumes = "application/json", produces = "application/json")
    public ResponseEntity<InstrumentUsage> startUsage(@RequestBody InstrumentUsage usage) {
        LOGGER.info("Starting usage for instrument {} by user {}",
                usage.getInstrumentId(), usage.getUserId());
        return ResponseEntity.ok(usageService.startUsage(usage));
    }

    @PostMapping(value = "/{usageId}/end", consumes = "application/json", produces = "application/json")
    public ResponseEntity<InstrumentUsage> endUsage(
            @PathVariable Long usageId,
            @RequestParam(required = false) String notes) {
        LOGGER.info("Ending usage with ID: {}", usageId);
        return ResponseEntity.ok(usageService.endUsage(usageId, notes));
    }

    @GetMapping(value = "/instrument/{instrumentId}", produces = "application/json")
    public ResponseEntity<List<InstrumentUsage>> getUsageByInstrument(
            @PathVariable String instrumentId) {
        LOGGER.info("Fetching usage for instrument: {}", instrumentId);
        return ResponseEntity.ok(usageService.getUsageByInstrument(instrumentId));
    }

    @GetMapping(value = "/user/{userId}",produces = "application/json")
    public ResponseEntity<List<InstrumentUsage>> getUsageByUser(
            @PathVariable String userId) {
        LOGGER.info("Fetching usage for user: {}", userId);
        return ResponseEntity.ok(usageService.getUsageByUser(userId));
    }

    @GetMapping(value = "/department/{department}",produces = "application/json")
    public ResponseEntity<List<InstrumentUsage>> getUsageByDepartment(
            @PathVariable String department) {
        LOGGER.info("Fetching usage for department: {}", department);
        return ResponseEntity.ok(usageService.getUsageByDepartment(department));
    }

    @GetMapping(value = "/location/{location}", produces = "application/json")
    public ResponseEntity<List<InstrumentUsage>> getUsageByLocation(
            @PathVariable String location) {
        LOGGER.info("Fetching usage for location: {}", location);
        return ResponseEntity.ok(usageService.getUsageByLocation(location));
    }

    @GetMapping(value = "/time-range",produces = "application/json")
    public ResponseEntity<List<InstrumentUsage>> getUsageByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        LOGGER.info("Fetching usage between {} and {}", start, end);
        return ResponseEntity.ok(usageService.getUsageByTimeRange(start, end));
    }

}
