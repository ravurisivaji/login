package com.ravuri.calibration.exception;

import org.apache.logging.log4j.Logger;

import java.io.Serial;

public class OfficeNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(OfficeNotFoundException.class);
    public OfficeNotFoundException(String message) {
        super(message);
        LOGGER.error(message);
    }
}
