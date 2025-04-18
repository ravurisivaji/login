package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // UserService interface for user-related operations
    public List<User> getAllUsers();
    public Optional<User> getUserById(Long id);
    public Optional<User> getUserByEmail(String email);
    public Optional<User> getUserByEmployeeId(String employeeID);
    public Optional<User> getUserByName(String name);

    public User createUser(User User);
    public User updateUser(Long id, User userDetails);
    public void deleteUser(Long id);
}
