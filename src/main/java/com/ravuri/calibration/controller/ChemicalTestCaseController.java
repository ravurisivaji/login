package com.ravuri.calibration.controller;


import com.ravuri.calibration.entity.ChemicalTestCase;
import com.ravuri.calibration.entity.TestCaseStatus;
import com.ravuri.calibration.service.ChemicalTestCaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chemical-test-cases")
public class ChemicalTestCaseController {

    private static final Logger LOGGER = LogManager.getLogger(ChemicalTestCaseController.class);

    @Autowired
    private ChemicalTestCaseService testCaseService;

    @PostMapping(value = "/distribute" , consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<ChemicalTestCase>> distributeTestCases(
            @RequestParam Long documentId,
            @RequestParam String department,
            @RequestParam String location,
            @RequestParam String assignedTo,
            @RequestHeader("X-User-Name") String assignedBy) {

        LOGGER.info("Distributing test cases from document {} to {} at {}/{}",
                documentId, assignedTo, department, location);

        return ResponseEntity.ok(testCaseService.distributeTestCases(
                documentId, department, location, assignedTo, assignedBy));
    }

    @GetMapping(value = "/department/{department}/location/{location}", produces = "application/json")
    public ResponseEntity<List<ChemicalTestCase>> getTestCasesByDepartmentAndLocation(
            @PathVariable String department,
            @PathVariable String location) {
        LOGGER.info("Fetching test cases for department {} and location {}", department, location);
        return ResponseEntity.ok(testCaseService.getTestCasesByDepartmentAndLocation(department, location));
    }

    @GetMapping(value = "/assignee/{assignedTo}", produces = "application/json")
    public ResponseEntity<List<ChemicalTestCase>> getTestCasesByAssignee(
            @PathVariable String assignedTo) {
        LOGGER.info("Fetching test cases assigned to {}", assignedTo);
        return ResponseEntity.ok(testCaseService.getTestCasesByAssignee(assignedTo));
    }

    @GetMapping(value = "/document/{documentId}", produces = "application/json")
    public ResponseEntity<List<ChemicalTestCase>> getTestCasesByDocument(
            @PathVariable Long documentId) {
        LOGGER.info("Fetching test cases for document {}", documentId);
        return ResponseEntity.ok(testCaseService.getTestCasesByDocument(documentId));
    }

    @PutMapping(value = "/{testCaseId}/status", produces = "application/json")
    public ResponseEntity<ChemicalTestCase> updateTestCaseStatus(
            @PathVariable Long testCaseId,
            @RequestParam TestCaseStatus status,
            @RequestParam(required = false) String notes) {
        LOGGER.info("Updating test case {} status to {}", testCaseId, status);
        return ResponseEntity.ok(testCaseService.updateTestCaseStatus(testCaseId, status, notes));
    }

}
