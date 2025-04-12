package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByEmail(String username);
    User findFirstByEmailAndPassword(String username, String password);

    User findFirstByEmployeeId(String employeeID);
}
