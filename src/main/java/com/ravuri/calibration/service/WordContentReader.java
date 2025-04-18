package com.ravuri.calibration.service;

import java.util.Map;

public interface WordContentReader {
    public Map<String, String> readContentById(Long documentId);
}
