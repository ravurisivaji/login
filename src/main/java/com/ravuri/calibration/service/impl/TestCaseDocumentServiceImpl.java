package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.TestDocument;
import com.ravuri.calibration.exception.DocumentNotFoundException;
import com.ravuri.calibration.exception.InvalidDocumentException;
import com.ravuri.calibration.exception.StorageException;
import com.ravuri.calibration.repository.TestDocumentRepository;
import com.ravuri.calibration.service.TestCaseDocumentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class TestCaseDocumentServiceImpl implements TestCaseDocumentService {

    private static final Logger LOGGER = LogManager.getLogger(TestCaseDocumentServiceImpl.class);
    @Autowired
    private TestDocumentRepository documentRepository;

    @Value("${document.storage.path}")
    private String storagePath;

    @Value("${document.max.size:10485760}") // 10MB default
    private long maxFileSize;

    @Transactional(readOnly = true)
    public List<TestDocument> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TestDocument getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + id));
    }

    @Transactional
    public TestDocument uploadDocument(MultipartFile file, String description, String username) {
        validateDocument(file);

        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = getFileExtension(fileName);

            if (!isWordDocument(fileExtension)) {
                throw new InvalidDocumentException("Only Microsoft Word documents (.doc, .docx) are allowed");
            }

            // Generate unique filename
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            Path targetLocation = getStoragePath().resolve(uniqueFileName);

            // Create directories if they don't exist
            Files.createDirectories(targetLocation.getParent());

            // Copy file to storage location
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Create document record
            TestDocument document = new TestDocument();
            document.setFileName(fileName);
            document.setFilePath(targetLocation.toString());
            document.setContentType(file.getContentType());
            document.setFileSize(file.getSize());
            document.setUploadDateTime(LocalDateTime.now());
            document.setDescription(description);
            document.setUploadedBy(username);
            document.setLastModifiedDateTime(LocalDateTime.now());
            document.setLastModifiedBy(username);

            TestDocument savedDocument = documentRepository.save(document);
            LOGGER.info("Document uploaded successfully: {}", savedDocument.getFileName());
            return savedDocument;

        } catch (IOException ex) {
            LOGGER.error("Failed to store document", ex);
            throw new StorageException("Failed to store document: " + ex.getMessage());
        }
    }

    @Transactional
    public TestDocument updateDocument(Long id, MultipartFile file, String description, String username) {
        TestDocument existingDocument = getDocumentById(id);
        validateDocument(file);

        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = getFileExtension(fileName);

            if (!isWordDocument(fileExtension)) {
                throw new InvalidDocumentException("Only Microsoft Word documents (.doc, .docx) are allowed");
            }

            // Generate unique filename
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            Path targetLocation = getStoragePath().resolve(uniqueFileName);

            // Delete old file
            try {
                Files.deleteIfExists(Paths.get(existingDocument.getFilePath()));
            } catch (IOException e) {
                LOGGER.warn("Could not delete old file: {}", existingDocument.getFilePath());
            }

            // Copy new file to storage location
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Update document record
            existingDocument.setFileName(fileName);
            existingDocument.setFilePath(targetLocation.toString());
            existingDocument.setContentType(file.getContentType());
            existingDocument.setFileSize(file.getSize());
            existingDocument.setDescription(description);
            existingDocument.setLastModifiedDateTime(LocalDateTime.now());
            existingDocument.setLastModifiedBy(username);

            TestDocument updatedDocument = documentRepository.save(existingDocument);
            LOGGER.info("Document updated successfully: {}", updatedDocument.getFileName());
            return updatedDocument;

        } catch (IOException ex) {
            LOGGER.error("Failed to update document", ex);
            throw new StorageException("Failed to update document: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Resource downloadDocument(Long id) {
        try {
            TestDocument document = getDocumentById(id);
            Path filePath = Paths.get(document.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                LOGGER.info("Document downloaded: {}", document.getFileName());
                return resource;
            } else {
                LOGGER.error("Document file not found: {}", document.getFilePath());
                throw new StorageException("Document file not found");
            }
        } catch (IOException ex) {
            LOGGER.error("Could not read document", ex);
            throw new StorageException("Could not read document: " + ex.getMessage());
        }
    }

    private void validateDocument(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidDocumentException("Document file cannot be empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new InvalidDocumentException("Document size exceeds maximum allowed size of " + maxFileSize + " bytes");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.contains("..")) {
            throw new InvalidDocumentException("Document name contains invalid path sequence: " + fileName);
        }
    }

    private Path getStoragePath() {
        return Paths.get(storagePath).toAbsolutePath().normalize();
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    }

    private boolean isWordDocument(String fileExtension) {
        return fileExtension.equals(".doc") || fileExtension.equals(".docx");
    }


}
