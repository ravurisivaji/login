package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.Chemical;
import com.ravuri.calibration.entity.ChemicalDistribution;

import java.time.LocalDateTime;
import java.util.List;

public interface ChemicalService {
    public Chemical createChemical(Chemical chemical, String username);
    public Chemical updateChemical(Long id, Chemical chemicalDetails, String username);
    public Chemical getChemicalById(Long id);
    public List<Chemical> getAllChemicals();
    public List<Chemical> getChemicalsByDepartment(String department);
    public List<Chemical> getChemicalsByLocation(String location);
    public List<Chemical> getChemicalsByReceiver(String receivedBy);
    public List<Chemical> getExpiredChemicals();
    public List<Chemical> getChemicalsByReceiptDate(LocalDateTime start, LocalDateTime end) ;
    public ChemicalDistribution issueChemical(ChemicalDistribution distribution, String issuedBy);
}
