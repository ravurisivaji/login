package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.ChemicalTestCase;
import com.ravuri.calibration.entity.TestCaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChemicalTestCaseRepository extends JpaRepository<ChemicalTestCase, Long> {
    List<ChemicalTestCase> findByDepartmentAndLocation(String department, String location);
    List<ChemicalTestCase> findByAssignedTo(String assignedTo);
    List<ChemicalTestCase> findByDocumentId(Long documentId);
    List<ChemicalTestCase> findByStatus(TestCaseStatus status);
}