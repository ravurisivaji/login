package com.ravuri.calibration.exception;

public class SupplierException extends RuntimeException {
    public SupplierException(String message) {
        super(message);
    }

    public SupplierException(String message, Throwable cause) {
        super(message, cause);
    }
}