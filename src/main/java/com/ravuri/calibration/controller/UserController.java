package com.ravuri.calibration.controller;

import com.ravuri.calibration.entity.Role;
import com.ravuri.calibration.service.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    /**
     * This class is used to manage user roles.
     * It provides endpoints to get all roles, get a role by ID, create a new role,
     * update an existing role, and delete a role.
     */

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);
    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/roles", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() {
        LOGGER.info("Fetching all roles");
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        LOGGER.info("Fetching role with ID: {}", id);
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/createRole", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        LOGGER.info("Creating new role: {}", role.getName());
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {
        LOGGER.info("Updating role with ID: {}", id);
        return ResponseEntity.ok(roleService.updateRole(id, roleDetails));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json",consumes = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        LOGGER.info("Deleting role with ID: {}", id);
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }
}
