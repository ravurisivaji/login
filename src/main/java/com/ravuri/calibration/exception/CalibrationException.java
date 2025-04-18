package com.ravuri.calibration.exception;

public class CalibrationException extends RuntimeException{

    public CalibrationException(String message) {
        super(message);
    }

    public CalibrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
