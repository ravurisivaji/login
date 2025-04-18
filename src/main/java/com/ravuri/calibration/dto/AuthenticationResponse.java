package com.ravuri.calibration.dto;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

//    private String jwt;
//
//    public AuthenticationResponse() {
//    }
//    public AuthenticationResponse(String jwt) {
//        this.jwt = jwt;
//    }

    private String token;
    private String username;
    private String role;
    private String employeeId;
    // Default constructor
    public AuthenticationResponse() {}

    public AuthenticationResponse(String token, String username, String role, String employeeId) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.employeeId = employeeId;
    }

    public AuthenticationResponse(String token, String username) {
        this.token = token;
        this.username = username;
  
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", employeeId='" + employeeId + '\'' +
                '}';
    }
}
