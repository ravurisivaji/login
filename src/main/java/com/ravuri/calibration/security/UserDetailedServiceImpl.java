package com.ravuri.calibration.security;

import com.ravuri.calibration.repository.UserRepository;
import com.ravuri.calibration.entity.User;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailedServiceImpl implements UserDetailsService {
    // Inject your user repository or any other service to fetch user details

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String employeeID) throws UsernameNotFoundException {
        // Here you can implement the logic to load the user from the database or any other source
//        User user = userRepository.findFirstByEmail(employeeID);
        User user = userRepository.findFirstByEmployeeId(employeeID);
        if( user == null) {
            throw new UsernameNotFoundException("User not found with employeeID: " + employeeID);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
