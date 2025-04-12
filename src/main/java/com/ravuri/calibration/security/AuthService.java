package com.ravuri.calibration.security;

import com.ravuri.calibration.dto.SignupRequest;
import com.ravuri.calibration.dto.UserDTO;

public interface AuthService {
    UserDTO createUser(SignupRequest signupRequest);
}
