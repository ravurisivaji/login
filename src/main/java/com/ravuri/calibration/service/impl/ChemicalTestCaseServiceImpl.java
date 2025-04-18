package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.ChemicalTestCase;
import com.ravuri.calibration.entity.TestCaseStatus;
import com.ravuri.calibration.entity.TestDocument;
import com.ravuri.calibration.exception.ChemicalValidationException;
import com.ravuri.calibration.exception.DocumentNotFoundException;
import com.ravuri.calibration.repository.ChemicalTestCaseRepository;
import com.ravuri.calibration.repository.TestDocumentRepository;
import com.ravuri.calibration.service.ChemicalTestCaseService;
import com.ravuri.calibration.service.WordContentReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChemicalTestCaseServiceImpl implements ChemicalTestCaseService {

    private static final Logger LOGGER = LogManager.getLogger(ChemicalTestCaseServiceImpl.class);

    private ChemicalTestCaseRepository testCaseRepository;
    private TestDocumentRepository documentRepository;
    private WordContentReader wordContentReader;

    private List<ChemicalTestCase> createTestCases(
            TestDocument document,
            Map<String, String> content,
            String department,
            String location,
            String assignedTo,
            String assignedBy) {

        List<ChemicalTestCase> testCases = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        content.forEach((key, value) -> {
            if (value.trim().isEmpty()) return;

            ChemicalTestCase testCase = new ChemicalTestCase();
            testCase.setDocument(document);
            testCase.setName(key);
            testCase.setDescription(value);
            testCase.setDepartment(department);
            testCase.setLocation(location);
            testCase.setAssignedTo(assignedTo);
            testCase.setAssignedBy(assignedBy);
            testCase.setAssignedAt(now);
            testCase.setStatus(TestCaseStatus.ASSIGNED);

            testCases.add(testCase);
        });

        return testCases;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChemicalTestCase> getTestCasesByDepartmentAndLocation(String department, String location) {
        validateDepartmentAndLocation(department, location);
        return testCaseRepository.findByDepartmentAndLocation(department, location);
    }

    @Override
    @Transactional
    public List<ChemicalTestCase> distributeTestCases(
            Long documentId,
            String department,
            String location,
            String assignedTo,
            String assignedBy) {
        try {
            validateInput(documentId, department, location, assignedTo, assignedBy);

            TestDocument document = documentRepository.findById(documentId)
                    .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + documentId));

            Map<String, String> documentContent = wordContentReader.readContentById(documentId);
            List<ChemicalTestCase> testCases = createTestCases(
                    document, documentContent, department, location, assignedTo, assignedBy);

            List<ChemicalTestCase> savedTestCases = testCaseRepository.saveAll(testCases);
            LOGGER.info("Distributed {} test cases from document {} to {} at {}/{}",
                    savedTestCases.size(), documentId, assignedTo, department, location);

            return savedTestCases;

        } catch (DocumentNotFoundException | ChemicalValidationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error distributing test cases", e);
            throw new ChemicalValidationException("Failed to distribute test cases: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChemicalTestCase> getTestCasesByAssignee(String assignedTo) {
        validateAssignee(assignedTo);
        return testCaseRepository.findByAssignedTo(assignedTo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChemicalTestCase> getTestCasesByDocument(Long documentId) {
        validateDocumentId(documentId);
        return testCaseRepository.findByDocumentId(documentId);
    }

    @Override
    @Transactional
    public ChemicalTestCase updateTestCaseStatus(
            Long testCaseId,
            TestCaseStatus status,
            String notes) {
        try {
            ChemicalTestCase testCase = testCaseRepository.findById(testCaseId)
                    .orElseThrow(() -> new ChemicalValidationException("Test case not found"));

            validateStatusTransition(testCase.getStatus(), status);

            testCase.setStatus(status);
            testCase.setNotes(notes);

            ChemicalTestCase updatedTestCase = testCaseRepository.save(testCase);
            LOGGER.info("Updated test case {} status to {}", testCaseId, status);
            return updatedTestCase;

        } catch (Exception e) {
            LOGGER.error("Error updating test case status", e);
            throw new ChemicalValidationException("Failed to update test case status: " + e.getMessage());
        }
    }

    private void validateInput(
            Long documentId,
            String department,
            String location,
            String assignedTo,
            String assignedBy) {

        validateDocumentId(documentId);
        validateDepartmentAndLocation(department, location);
        validateAssignee(assignedTo);
        validateAssigner(assignedBy);
    }

    private void validateDocumentId(Long documentId) {
        if (documentId == null) {
            throw new ChemicalValidationException("Document ID is required");
        }
    }

    private void validateDepartmentAndLocation(String department, String location) {
        if (department == null || department.trim().isEmpty()) {
            throw new ChemicalValidationException("Department is required");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new ChemicalValidationException("Location is required");
        }
    }

    private void validateAssignee(String assignedTo) {
        if (assignedTo == null || assignedTo.trim().isEmpty()) {
            throw new ChemicalValidationException("Assignee is required");
        }
    }

    private void validateAssigner(String assignedBy) {
        if (assignedBy == null || assignedBy.trim().isEmpty()) {
            throw new ChemicalValidationException("Assigner is required");
        }
    }

    private void validateStatusTransition(
            TestCaseStatus currentStatus,
            TestCaseStatus newStatus) {

        if (currentStatus == TestCaseStatus.COMPLETED ||
                currentStatus == TestCaseStatus.REJECTED) {
            throw new ChemicalValidationException(
                    "Cannot update status of test case that is already " + currentStatus);
        }
    }



}
