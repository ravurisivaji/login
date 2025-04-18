package com.ravuri.calibration.exception;

public class ConcurrentUsageException extends RuntimeException {
    public ConcurrentUsageException(String message) {
        super(message);
    }

    public ConcurrentUsageException(String message, Throwable cause) {
        super(message, cause);
    }
}