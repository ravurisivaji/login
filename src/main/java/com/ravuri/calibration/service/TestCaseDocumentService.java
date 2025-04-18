package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.TestDocument;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.util.List;

public interface TestCaseDocumentService {
    public List<TestDocument> getAllDocuments();
    public TestDocument getDocumentById(Long id);
    public TestDocument uploadDocument(MultipartFile file, String description, String username);
    public TestDocument updateDocument(Long id, MultipartFile file, String description, String username);
    public Resource downloadDocument(Long id);
}
