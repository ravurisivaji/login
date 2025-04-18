package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.ChemicalTestCase;
import com.ravuri.calibration.entity.TestCaseStatus;
import com.ravuri.calibration.entity.TestDocument;

import java.util.List;
import java.util.Map;

public interface ChemicalTestCaseService {

    public List<ChemicalTestCase> distributeTestCases(
            Long documentId,
            String department,
            String location,
            String assignedTo,
            String assignedBy);

    public List<ChemicalTestCase> getTestCasesByDepartmentAndLocation(String department, String location);
    public List<ChemicalTestCase> getTestCasesByAssignee(String assignedTo);
    public List<ChemicalTestCase> getTestCasesByDocument(Long documentId);
    public ChemicalTestCase updateTestCaseStatus(
            Long testCaseId,
            TestCaseStatus status,
            String notes)
        ;


}
