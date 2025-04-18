package com.ravuri.calibration.exception;

import jssc.SerialPortException;

public class InstrumentConnectionException extends RuntimeException {
    public InstrumentConnectionException(String message) {
        super(message);
    }

    public InstrumentConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}