package com.ravuri.calibration.controller;

import com.ravuri.calibration.entity.Chemical;
import com.ravuri.calibration.service.ChemicalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chemicals")
public class ChemicalController {
    private static final Logger LOGGER = LogManager.getLogger(ChemicalController.class);

    @Autowired
    private ChemicalService chemicalService;

    @PostMapping
    public ResponseEntity<Chemical> createChemical(
            @RequestBody Chemical chemical,
            @RequestHeader("X-User-Name") String username) {
        LOGGER.info("Creating new chemical: {} by user: {}", chemical.getName(), username);
        return ResponseEntity.ok(chemicalService.createChemical(chemical, username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chemical> updateChemical(
            @PathVariable Long id,
            @RequestBody Chemical chemical,
            @RequestHeader("X-User-Name") String username) {
        LOGGER.info("Updating chemical with id: {} by user: {}", id, username);
        return ResponseEntity.ok(chemicalService.updateChemical(id, chemical, username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chemical> getChemical(@PathVariable Long id) {
        LOGGER.info("Fetching chemical with id: {}", id);
        return ResponseEntity.ok(chemicalService.getChemicalById(id));
    }

    @GetMapping
    public ResponseEntity<List<Chemical>> getAllChemicals() {
        LOGGER.info("Fetching all chemicals");
        return ResponseEntity.ok(chemicalService.getAllChemicals());
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<Chemical>> getChemicalsByDepartment(
            @PathVariable String department) {
        LOGGER.info("Fetching chemicals for department: {}", department);
        return ResponseEntity.ok(chemicalService.getChemicalsByDepartment(department));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Chemical>> getChemicalsByLocation(
            @PathVariable String location) {
        LOGGER.info("Fetching chemicals for location: {}", location);
        return ResponseEntity.ok(chemicalService.getChemicalsByLocation(location));
    }

    @GetMapping("/receiver/{receivedBy}")
    public ResponseEntity<List<Chemical>> getChemicalsByReceiver(
            @PathVariable String receivedBy) {
        LOGGER.info("Fetching chemicals received by: {}", receivedBy);
        return ResponseEntity.ok(chemicalService.getChemicalsByReceiver(receivedBy));
    }

    @GetMapping("/expired")
    public ResponseEntity<List<Chemical>> getExpiredChemicals() {
        LOGGER.info("Fetching expired chemicals");
        return ResponseEntity.ok(chemicalService.getExpiredChemicals());
    }

    @GetMapping("/receipt-date")
    public ResponseEntity<List<Chemical>> getChemicalsByReceiptDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        LOGGER.info("Fetching chemicals received between {} and {}", start, end);
        return ResponseEntity.ok(chemicalService.getChemicalsByReceiptDate(start, end));
    }

}
