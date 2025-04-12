package com.ravuri.calibration.dto;

public class AuthenticationRequest {
    private String employeeId;
    private String password;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthenticationRequest{" +
                "email='" + employeeId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
