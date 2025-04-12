package com.ravuri.calibration.controller;

import com.ravuri.calibration.dto.SignupRequest;
import com.ravuri.calibration.dto.UserDTO;
import com.ravuri.calibration.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class SignUpUserController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody SignupRequest signupRequest) {

        UserDTO createdUser = authService.createUser(signupRequest);

        if(createdUser == null) {
            return ResponseEntity.badRequest().body("User creation failed try again");
        }
        // Logic to create a user
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
