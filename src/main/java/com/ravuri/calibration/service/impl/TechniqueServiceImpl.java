package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.Technique;
import com.ravuri.calibration.exception.DuplicateTechniqueException;
import com.ravuri.calibration.exception.TechniqueNotFoundException;
import com.ravuri.calibration.exception.ValidationException;
import com.ravuri.calibration.repository.TechniqueRepository;
import com.ravuri.calibration.service.TechniqueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TechniqueServiceImpl implements TechniqueService {

    private static final Logger LOGGER = LogManager.getLogger(TechniqueServiceImpl.class);

    @Autowired
    private TechniqueRepository techniqueRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Technique> getAllTechniques() {
        LOGGER.info("Fetching all techniques");
        return techniqueRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Technique getTechniqueById(Long id) {
        LOGGER.info("Fetching technique with ID: {}", id);
        return techniqueRepository.findById(id)
                .orElseThrow(() -> new TechniqueNotFoundException("Technique not found with id: " + id));
    }

    @Override
    @Transactional
    public Technique createTechnique(Technique technique) {
        LOGGER.info("Creating new technique: {}", technique);


        validateTechnique(technique);
        if (techniqueRepository.existsByName(technique.getName())) {
            throw new DuplicateTechniqueException("Technique with name " + technique.getName() + " already exists");
        }

        return techniqueRepository.save(technique);
    }

    @Override
    @Transactional
    public Technique updateTechnique(Long id, Technique techniqueDetails) {
        LOGGER.info("Updating technique with ID: {} with details: {}", id, techniqueDetails);
        Technique technique = getTechniqueById(id);

        validateTechnique(techniqueDetails);

        if (!technique.getName().equals(techniqueDetails.getName()) &&
                techniqueRepository.existsByName(techniqueDetails.getName())) {
            throw new DuplicateTechniqueException("Technique with name " + techniqueDetails.getName() + " already exists");
        }

        technique.setName(techniqueDetails.getName());
        technique.setDescription(techniqueDetails.getDescription());
        technique.setMethodology(techniqueDetails.getMethodology());
        technique.setApplicableDrugTypes(techniqueDetails.getApplicableDrugTypes());
        technique.setRequiredInstruments(techniqueDetails.getRequiredInstruments());
        technique.setDetectionLimitInNanograms(techniqueDetails.getDetectionLimitInNanograms());
        technique.setTypicalAnalysisTimeInMinutes(techniqueDetails.getTypicalAnalysisTimeInMinutes());

        return techniqueRepository.save(technique);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Technique> getTechniquesByDrugType(String drugType) {
        LOGGER.info("Fetching techniques applicable for drug type: {}", drugType);

        if (drugType == null || drugType.trim().isEmpty()) {
            throw new ValidationException("Drug type cannot be empty");
        }
        return techniqueRepository.findByApplicableDrugTypesContaining(drugType);
    }

    private void validateTechnique(Technique technique) {
        if (technique.getDetectionLimitInNanograms() == null) {
            throw new ValidationException("Detection limit is required");
        }

        if (technique.getDetectionLimitInNanograms() <= 0) {
            throw new ValidationException("Detection limit must be greater than 0");
        }

        if (technique.getTypicalAnalysisTimeInMinutes() == null) {
            throw new ValidationException("Typical analysis time is required");
        }

        if (technique.getTypicalAnalysisTimeInMinutes() <= 0) {
            throw new ValidationException("Typical analysis time must be greater than 0");
        }

        if (technique.getApplicableDrugTypes() == null || technique.getApplicableDrugTypes().isEmpty()) {
            throw new ValidationException("At least one applicable drug type is required");
        }
    }
}
