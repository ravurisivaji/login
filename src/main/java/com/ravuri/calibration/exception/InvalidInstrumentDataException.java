package com.ravuri.calibration.exception;

public class InvalidInstrumentDataException extends RuntimeException {
    public InvalidInstrumentDataException(String message) {
        super(message);
    }

    public InvalidInstrumentDataException(String message, Throwable cause) {
        super(message, cause);
    }
}