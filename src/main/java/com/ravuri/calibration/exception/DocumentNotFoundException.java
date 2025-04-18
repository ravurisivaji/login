package com.ravuri.calibration.exception;

public class DocumentNotFoundException  extends RuntimeException {
    public DocumentNotFoundException(String message) {
        super(message);
    }
}