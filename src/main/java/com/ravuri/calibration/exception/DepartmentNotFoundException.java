package com.ravuri.calibration.exception;

public class DepartmentNotFoundException  extends RuntimeException {
    public DepartmentNotFoundException(String message) {
        super(message);
    }
}