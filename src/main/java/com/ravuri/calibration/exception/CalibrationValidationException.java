package com.ravuri.calibration.exception;

public class CalibrationValidationException extends RuntimeException{

    public CalibrationValidationException(String message) {
        super(message);
    }

    public CalibrationValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
