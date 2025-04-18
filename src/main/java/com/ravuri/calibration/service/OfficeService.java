package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.Office;
import com.ravuri.calibration.entity.OfficeType;

import java.util.List;

public interface OfficeService {
    public List<Office> getAllOffices();
    public Office getOfficeById(Long id);
    public Office createOffice(Office office);
    public Office updateOffice(Long id, Office officeDetails);
    public void deleteOffice(Long id);
    public List<Office> getOfficesByType(OfficeType type);
    public List<Office> getOfficesByCountry(String country);
    public List<Office> getOfficesByState(String state);
    public List<Office> getOfficesByCity(String city);
}
