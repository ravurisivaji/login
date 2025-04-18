package com.ravuri.calibration.exception;

public class UsageNotFoundException extends RuntimeException {
    public UsageNotFoundException(String message) {
        super(message);
    }

    public UsageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}