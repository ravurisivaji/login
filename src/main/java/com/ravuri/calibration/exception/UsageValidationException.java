package com.ravuri.calibration.exception;

public class UsageValidationException extends RuntimeException {
    public UsageValidationException(String message) {
        super(message);
    }

    public UsageValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}