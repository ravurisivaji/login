package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.ChemicalDistribution;

import java.time.LocalDateTime;
import java.util.List;

public interface ChemicalDistributionService {
    public ChemicalDistribution issueChemical(ChemicalDistribution distribution, String issuedBy);
    public ChemicalDistribution returnChemical(Long distributionId, Double returnedQuantity, String returnedBy);
    public List<ChemicalDistribution> getDistributionsByChemical(Long chemicalId);
    public List<ChemicalDistribution> getDistributionsByDepartment(String department);
    public List<ChemicalDistribution> getDistributionsByLocation(String location);
    public List<ChemicalDistribution> getDistributionsByDateRange(LocalDateTime start, LocalDateTime end);
    public List<ChemicalDistribution> getDistributionsByUser(String issuedTo);
}
