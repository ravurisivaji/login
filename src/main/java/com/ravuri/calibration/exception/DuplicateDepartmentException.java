package com.ravuri.calibration.exception;

import java.io.Serial;

public class DuplicateDepartmentException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateDepartmentException(String message) {
        super(message);
    }

    public DuplicateDepartmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateDepartmentException(Throwable cause) {
        super(cause);
    }
}
