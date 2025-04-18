package com.ravuri.calibration.security;

import com.ravuri.calibration.entity.Role;
import com.ravuri.calibration.repository.UserRepository;
import com.ravuri.calibration.entity.User;

import com.ravuri.calibration.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailedServiceImpl implements UserDetailsService {
    // Inject your user repository or any other service to fetch user details

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleService roleService;
//    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Here you can implement the logic to load the roles from the database or any other source
        List<Role> roles = roleService.getAllRoles();

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
    @Override
    public UserDetails loadUserByUsername(String employeeID) throws UsernameNotFoundException {
        // Here you can implement the logic to load the user from the database or any other source
//        User user = userRepository.findFirstByEmail(employeeID);
        Optional<User> user = userRepository.findFirstByEmployeeId(employeeID);
        if( user.get() == null) {
            throw new UsernameNotFoundException("User not found with employeeID: " + employeeID);
        }
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), new ArrayList<>());
    }
}
