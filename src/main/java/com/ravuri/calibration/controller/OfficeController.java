package com.ravuri.calibration.controller;


import com.ravuri.calibration.entity.Office;
import com.ravuri.calibration.entity.OfficeType;
import com.ravuri.calibration.service.OfficeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/offices")
@RequiredArgsConstructor
public class OfficeController {

    private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(OfficeController.class);
    @Autowired
    private OfficeService officeService;

    @GetMapping
    public ResponseEntity<List<Office>> getAllOffices() {
        LOGGER.info("Fetching all offices");
        return ResponseEntity.ok(officeService.getAllOffices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Office> getOfficeById(@PathVariable Long id) {
        LOGGER.info("Fetching office with ID: {}", id);
        return ResponseEntity.ok(officeService.getOfficeById(id));
    }

    @PostMapping
    public ResponseEntity<Office> createOffice(@Valid @RequestBody Office office) {
        LOGGER.info("Creating new office: {}", office);
        return ResponseEntity.ok(officeService.createOffice(office));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Office> updateOffice(
            @PathVariable Long id,
            @Valid @RequestBody Office office) {
        LOGGER.info("Updating office with ID: {} with details: {}", id, office);
        return ResponseEntity.ok(officeService.updateOffice(id, office));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffice(@PathVariable Long id) {
        LOGGER.info("Deleting office with ID: {}", id);
        officeService.deleteOffice(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Office>> getOfficesByType(
            @PathVariable OfficeType type) {
        LOGGER.info("Fetching offices of type: {}", type);
        return ResponseEntity.ok(officeService.getOfficesByType(type));
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<Office>> getOfficesByCountry(
            @PathVariable String country) {
        LOGGER.info("Fetching offices in country: {}", country);
        return ResponseEntity.ok(officeService.getOfficesByCountry(country));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Office>> getOfficesByState(
            @PathVariable String state) {
        LOGGER.info("Fetching offices in state: {}", state);
        return ResponseEntity.ok(officeService.getOfficesByState(state));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Office>> getOfficesByCity(
            @PathVariable String city) {
        LOGGER.info("Fetching offices in city: {}", city);
        return ResponseEntity.ok(officeService.getOfficesByCity(city));
    }
}
