package com.ravuri.calibration.controller;

import com.ravuri.calibration.entity.Department;
import com.ravuri.calibration.entity.DepartmentType;
import com.ravuri.calibration.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serial;
import java.io.Serializable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@SecurityRequirement(name = "bearerAuth")
@EnableGlobalAuthentication

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(DepartmentController.class);
    @Autowired
    private DepartmentService departmentService;

    @GetMapping(value = "/allDepartments", produces = "application/json")
    public ResponseEntity<List<Department>> getAllDepartments() {
        LOGGER.info("Fetching all departments");
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping(value = "/{id}", produces =  "application/json")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        LOGGER.info("Fetching department with ID: {}", id);
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/code/{code}", produces = "application/json")
    public ResponseEntity<Department> getDepartmentByCode(@PathVariable String code) {
        return ResponseEntity.ok(departmentService.getDepartmentByCode(code));
    }


    @PostMapping(value = "/createDepartment", consumes = "application/json", produces = "application/json")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        LOGGER.info("Creating new department: {}", department.getName());
        return ResponseEntity.ok(departmentService.createDepartment(department));
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id,
                                                       @RequestBody Department department) {
        LOGGER.info("Updating department with ID: {}", id);
        return ResponseEntity.ok(departmentService.updateDepartment(id, department));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        LOGGER.info("Deleting department with ID: {}", id);
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/type/{type}", produces = "application/json")
    public ResponseEntity<List<Department>> getDepartmentsByType(
            @PathVariable DepartmentType type) {
        return ResponseEntity.ok(departmentService.getDepartmentsByType(type));
    }
}
