package com.ravuri.calibration.exception;

public class InstrumentException extends RuntimeException{

    public InstrumentException(String message) {
        super(message);
    }

    public InstrumentException(String message, Throwable cause) {
        super(message, cause);
    }

}
