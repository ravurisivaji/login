package com.ravuri.calibration.controller;

import com.ravuri.calibration.service.WordContentReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/word-content")
public class WordContentController {

    private static final Logger LOGGER = LogManager.getLogger(WordContentController.class);
    @Autowired
    private WordContentReader wordContentReader;

    @GetMapping("/{documentId}")
    public ResponseEntity<Map<String, String>> readWordContentById(@PathVariable Long documentId) {
        LOGGER.info("Reading content from document with ID: {}", documentId);
        return ResponseEntity.ok(wordContentReader.readContentById(documentId));
    }

}
