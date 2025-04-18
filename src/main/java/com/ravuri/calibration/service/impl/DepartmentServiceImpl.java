package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.Department;
import com.ravuri.calibration.entity.DepartmentType;
import com.ravuri.calibration.exception.DepartmentNotFoundException;
import com.ravuri.calibration.exception.DuplicateDepartmentException;
import com.ravuri.calibration.exception.ValidationException;
import com.ravuri.calibration.repository.DepartmentRepository;
import com.ravuri.calibration.service.DepartmentService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(DepartmentServiceImpl.class);

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        LOGGER.info("Fetching all departments");
        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Department> getDepartmentById(Long id) {
        LOGGER.info("Fetching department with ID: {}", id);
        return departmentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Department getDepartmentByCode(String code) {
        return departmentRepository.findByCode(code)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with code: " + code));
    }

    @Transactional
    public Department createDepartment(Department department) {
        LOGGER.info("Creating new department: {}", department.getName());

        validateDepartment(department);
        checkDuplicates(department);

        return departmentRepository.save(department);
    }

    @Transactional
    public Department updateDepartment(Long id, Department departmentDetails) {
        LOGGER.info("Updating department with ID: {}", id);
        Department department = getDepartmentById(id).get();
        validateDepartment(departmentDetails);

        if (!department.getCode().equals(departmentDetails.getCode()) &&
                departmentRepository.existsByCode(departmentDetails.getCode())) {
            throw new DuplicateDepartmentException("Department with code " + departmentDetails.getCode() + " already exists");
        }

        if (!department.getName().equals(departmentDetails.getName()) &&
                departmentRepository.existsByName(departmentDetails.getName())) {
            throw new DuplicateDepartmentException("Department with name " + departmentDetails.getName() + " already exists");
        }

        department.setName(departmentDetails.getName());
        department.setCode(departmentDetails.getCode());
        department.setDescription(departmentDetails.getDescription());
        department.setManagerId(departmentDetails.getManagerId());

        department.setType(departmentDetails.getType());
        department.setOffice(departmentDetails.getOffice());
        department.setFloor(departmentDetails.getFloor());
        department.setWing(departmentDetails.getWing());
        department.setExtension(departmentDetails.getExtension());

        return departmentRepository.save(department);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        LOGGER.info("Deleting department with ID: {}", id);
        if (!departmentRepository.existsById(id)) {
            throw new DepartmentNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }

    @Override
    public Optional<Department> getDepartmentByName(String name) {
        LOGGER.info("Fetching department with name: {}", name);
        return departmentRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> getDepartmentsByType(DepartmentType type) {
        if (type == null) {
            throw new ValidationException("Department type cannot be null");
        }
        return departmentRepository.findByType(type);
    }





    private void validateDepartment(Department department) {

        if (department.getType() == null) {
            throw new ValidationException("Department type is required");
        }

        if (department.getOffice() == null) {
            throw new ValidationException("Office location is required");
        }

        if (department.getFloor() == null || department.getFloor().trim().isEmpty()) {
            throw new ValidationException("Floor number/name is required");
        }

        if (department.getWing() == null || department.getWing().trim().isEmpty()) {
            throw new ValidationException("Wing information is required");
        }
    }

    private void checkDuplicates(Department department) {
        if (departmentRepository.existsByCode(department.getCode())) {
            throw new DuplicateDepartmentException("Department with code " + department.getCode() + " already exists");
        }

        if (departmentRepository.existsByName(department.getName())) {
            throw new DuplicateDepartmentException("Department with name " + department.getName() + " already exists");
        }
    }
}
