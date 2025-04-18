package com.ravuri.calibration.controller;

import com.ravuri.calibration.entity.TestDocument;
import com.ravuri.calibration.service.TestCaseDocumentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/v1/documents")
public class TestDocumentController {

    private static final Logger LOGGER = LogManager.getLogger(TestDocumentController.class);

    @Autowired
    private TestCaseDocumentService documentService;

    @GetMapping
    public ResponseEntity<List<TestDocument>> getAllDocuments() {
        LOGGER.info("Fetching all documents");
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestDocument> getDocumentById(@PathVariable Long id) {
        LOGGER.info("Fetching document with id: {}", id);
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TestDocument> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @RequestHeader("X-User-Name") String username) {
        LOGGER.info("Uploading document: {} by user: {}", file.getOriginalFilename(), username);
        return ResponseEntity.ok(documentService.uploadDocument(file, description, username));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TestDocument> updateDocument(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @RequestHeader("X-User-Name") String username) {
        LOGGER.info("Updating document with id: {} by user: {}", id, username);
        return ResponseEntity.ok(documentService.updateDocument(id, file, description, username));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        LOGGER.info("Downloading document with id: {}", id);
        TestDocument document = documentService.getDocumentById(id);
        Resource resource = documentService.downloadDocument(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                .body(resource);
    }

}
