package com.ravuri.calibration.exception;

public class InstrumentNotFoundException extends RuntimeException {
    public InstrumentNotFoundException(String message) {
        super(message);
    }
}