package com.ravuri.calibration.exception;

import java.io.Serial;

public class DuplicateOfficeException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateOfficeException(String message) {
        super(message);
    }

    public DuplicateOfficeException(String message, Throwable cause) {
        super(message, cause);
    }
}
