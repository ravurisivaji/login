package com.ravuri.calibration.controller;

import com.ravuri.calibration.entity.Technique;
import com.ravuri.calibration.service.TechniqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/techniques")
public class TechniqueController {
    private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(TechniqueController.class);
    @Autowired
    private TechniqueService techniqueService;

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Technique>> getAllTechniques() {
        LOGGER.info("Fetching all techniques");
        return ResponseEntity.ok(techniqueService.getAllTechniques());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Technique> getTechniqueById(@PathVariable Long id) {
        LOGGER.info("Fetching technique with ID: {}", id);
        return ResponseEntity.ok(techniqueService.getTechniqueById(id));
    }

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Technique> createTechnique(@Valid @RequestBody Technique technique) {
        LOGGER.info("Creating new technique: {}", technique);
        return ResponseEntity.ok(techniqueService.createTechnique(technique));
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Technique> updateTechnique(
            @PathVariable Long id,
            @Valid @RequestBody Technique technique) {
        LOGGER.info("Updating technique with ID: {} with details: {}", id, technique);
        return ResponseEntity.ok(techniqueService.updateTechnique(id, technique));
    }

    @GetMapping(value = "/drug-type/{drugType}", produces = "application/json")
    public ResponseEntity<List<Technique>> getTechniquesByDrugType(@PathVariable String drugType) {
        LOGGER.info("Fetching techniques for drug type: {}", drugType);
        return ResponseEntity.ok(techniqueService.getTechniquesByDrugType(drugType));
    }
}
