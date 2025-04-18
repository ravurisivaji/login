package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.Department;
import com.ravuri.calibration.entity.DepartmentType;
import com.ravuri.calibration.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);

    boolean existsByName(String name);


    Optional<Department> findByCode(String code);

    boolean existsByCode(String code);

    List<Department> findByType(DepartmentType type);

    List<Department> findByOffice(Office office);

    List<Department> findByOfficeAndFloor(Office office, String floor);

    List<Department> findByOfficeAndWing(Office office, String wing);
}
