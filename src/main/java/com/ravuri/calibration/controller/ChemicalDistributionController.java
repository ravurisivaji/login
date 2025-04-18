package com.ravuri.calibration.controller;


import com.ravuri.calibration.entity.ChemicalDistribution;
import com.ravuri.calibration.service.ChemicalDistributionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chemical-distributions")
public class ChemicalDistributionController {

    private static final Logger LOGGER = LogManager.getLogger(ChemicalDistributionController.class);

    @Autowired
    private ChemicalDistributionService distributionService;

    @PostMapping("/issue")
    public ResponseEntity<ChemicalDistribution> issueChemical(
            @RequestBody ChemicalDistribution distribution,
            @RequestHeader("X-User-Name") String issuedBy) {
        LOGGER.info("Issuing chemical {} to {}", distribution.getChemical().getId(), distribution.getIssuedTo());
        return ResponseEntity.ok(distributionService.issueChemical(distribution, issuedBy));
    }

    @PostMapping("/{distributionId}/return")
    public ResponseEntity<ChemicalDistribution> returnChemical(
            @PathVariable Long distributionId,
            @RequestParam Double returnedQuantity,
            @RequestHeader("X-User-Name") String returnedBy) {
        LOGGER.info("Processing return for distribution {}", distributionId);
        return ResponseEntity.ok(distributionService.returnChemical(distributionId, returnedQuantity, returnedBy));
    }

    @GetMapping("/chemical/{chemicalId}")
    public ResponseEntity<List<ChemicalDistribution>> getDistributionsByChemical(
            @PathVariable Long chemicalId) {
        LOGGER.info("Fetching distributions for chemical: {}", chemicalId);
        return ResponseEntity.ok(distributionService.getDistributionsByChemical(chemicalId));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<ChemicalDistribution>> getDistributionsByDepartment(
            @PathVariable String department) {
        LOGGER.info("Fetching distributions for department: {}", department);
        return ResponseEntity.ok(distributionService.getDistributionsByDepartment(department));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<ChemicalDistribution>> getDistributionsByLocation(
            @PathVariable String location) {
        LOGGER.info("Fetching distributions for location: {}", location);
        return ResponseEntity.ok(distributionService.getDistributionsByLocation(location));
    }

    @GetMapping("/user/{issuedTo}")
    public ResponseEntity<List<ChemicalDistribution>> getDistributionsByUser(
            @PathVariable String issuedTo) {
        LOGGER.info("Fetching distributions for user: {}", issuedTo);
        return ResponseEntity.ok(distributionService.getDistributionsByUser(issuedTo));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ChemicalDistribution>> getDistributionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        LOGGER.info("Fetching distributions between {} and {}", start, end);
        return ResponseEntity.ok(distributionService.getDistributionsByDateRange(start, end));
    }

}
