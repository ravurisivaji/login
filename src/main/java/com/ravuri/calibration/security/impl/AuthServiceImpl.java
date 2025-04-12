package com.ravuri.calibration.security.impl;

import com.ravuri.calibration.dto.SignupRequest;
import com.ravuri.calibration.dto.UserDTO;
import com.ravuri.calibration.entity.User;
import com.ravuri.calibration.repository.UserRepository;
import com.ravuri.calibration.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO createUser(SignupRequest signupRequest) {
        // Logic to create a user
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPhone(signupRequest.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setEmployeeId(signupRequest.getEmployeeId());
        User createdUser = userRepository.save(user);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(createdUser.getEmail());
        userDTO.setPhone(createdUser.getPhone());
        userDTO.setUsername(createdUser.getUsername());
        userDTO.setEmployeeId(createdUser.getEmployeeId());
        return userDTO;
    }
}
