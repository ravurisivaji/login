package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.Technique;

import java.util.List;

public interface TechniqueService {

    public List<Technique> getAllTechniques();
    public Technique getTechniqueById(Long id);
    public Technique createTechnique(Technique technique);
    public Technique updateTechnique(Long id, Technique techniqueDetails);
    public List<Technique> getTechniquesByDrugType(String drugType);
}
