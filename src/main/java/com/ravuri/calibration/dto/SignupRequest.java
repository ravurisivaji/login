package com.ravuri.calibration.dto;

import com.ravuri.calibration.entity.Role;

public class SignupRequest {

    private String username;
    private String email;
    private String phone;
    private String password;
    private String employeeId;
    private String role = "ROLE_USER";

    public SignupRequest() {
    }

    public SignupRequest(String username, String email, String phone, String employeeId, String role) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.employeeId = employeeId;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
