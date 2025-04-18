package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.TestDocument;
import com.ravuri.calibration.exception.DocumentNotFoundException;
import com.ravuri.calibration.exception.DocumentProcessingException;
import com.ravuri.calibration.repository.TestDocumentRepository;
import com.ravuri.calibration.service.WordContentReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class WordContentReaderImpl  implements WordContentReader {

    private static final Logger LOGGER = LogManager.getLogger(WordContentReaderImpl.class);
    @Autowired
    private TestDocumentRepository documentRepository;

    public Map<String, String> readContentById(Long documentId) {
        TestDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + documentId));

        Path documentPath = Paths.get(document.getFilePath());
        FileSystemResource resource = new FileSystemResource(documentPath);

        if (!resource.exists()) {
            throw new DocumentNotFoundException("Document file not found: " + document.getFileName());
        }

        try (FileInputStream fis = new FileInputStream(documentPath.toFile())) {
            XWPFDocument wordDocument = new XWPFDocument(fis);
            Map<String, String> contentMap = new LinkedHashMap<>();
            int paragraphCount = 0;
            int tableCount = 0;

            // Process paragraphs
            for (XWPFParagraph paragraph : wordDocument.getParagraphs()) {
                String text = paragraph.getText().trim();
                if (!text.isEmpty()) {
                    paragraphCount++;
                    contentMap.put("Paragraph-" + paragraphCount, text);
                }
            }

            // Process tables
            for (XWPFTable table : wordDocument.getTables()) {
                tableCount++;
                StringBuilder tableContent = new StringBuilder();

                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        tableContent.append(cell.getText().trim()).append("\t");
                    }
                    tableContent.append("\n");
                }

                contentMap.put("Table-" + tableCount, tableContent.toString().trim());
            }

            wordDocument.close();
            LOGGER.info("Successfully processed document {}. Found {} paragraphs and {} tables",
                    document.getFileName(), paragraphCount, tableCount);
            return contentMap;

        } catch (IOException e) {
            LOGGER.error("Error reading Word document: {}", document.getFileName(), e);
            throw new DocumentProcessingException("Failed to read Word document: " + e.getMessage());
        }
    }

}
