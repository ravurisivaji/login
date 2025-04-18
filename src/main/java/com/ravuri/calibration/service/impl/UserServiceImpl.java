package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.User;
import com.ravuri.calibration.repository.UserRepository;
import com.ravuri.calibration.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger Logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserById(Long id) {
        Logger.info("Fetching user with ID: {}", id);
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findFirstByEmail(email));
    }

    @Override
    public Optional<User> getUserByEmployeeId(String employeeID) {
        return userRepository.findFirstByEmployeeId(employeeID);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserByName(String name) {
        Logger.info("Fetching user with name: {}", name);
        return userRepository.findByUsername(name);
    }

    @Transactional
    @Override
    public User createUser(User User) {
        Logger.info("Creating new user: {}", User.getUsername());

        if (userRepository.findByUsername(User.getUsername()).isPresent()) {
            throw new RuntimeException("User with name " + User.getUsername() + " already exists");
        }
        return userRepository.save(User);
    }

    @Transactional
    @Override
    public User updateUser(Long id, User userDetails) {
        Logger.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        // Check if new name conflicts with existing user (excluding current user)
        Optional<User> existingUser = userRepository.findByUsername(userDetails.getUsername());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
            throw new RuntimeException("User with name " + userDetails.getUsername() + " already exists");
        }
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setEmployeeId(userDetails.getEmployeeId());
        user.setRole(userDetails.getRole());
        user.setPassword(userDetails.getPassword());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {

        Logger.info("Deleting user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        // Logic to delete user
        if(user.getRole() != null) {
            throw new RuntimeException("Cannot delete user as it is assigned to a role");
        }
        userRepository.deleteById(id);
    }
}
