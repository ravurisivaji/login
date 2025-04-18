package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.Department;
import com.ravuri.calibration.entity.DepartmentType;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    public List<Department> getAllDepartments();
    public Optional<Department> getDepartmentById(Long id);
    public Department createDepartment(Department department);
    public Department updateDepartment(Long id, Department departmentDetails);
    public void deleteDepartment(Long id);
    public Optional<Department> getDepartmentByName(String name);
    public List<Department> getDepartmentsByType(DepartmentType type);
    public Department getDepartmentByCode(String code);
}
