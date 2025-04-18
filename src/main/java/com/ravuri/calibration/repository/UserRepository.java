package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByEmail(String username);
    User findFirstByUsername(String username);
    User findFirstByUsernameAndPassword(String username, String password);
    Optional<User> findFirstByEmployeeId(String employeeID);
    User findFirstByEmailAndPassword(String username, String password);


    Optional<User> findByUsername(String username);
}
