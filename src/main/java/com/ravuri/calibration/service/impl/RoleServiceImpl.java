package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.Role;
import com.ravuri.calibration.repository.RoleRepository;
import com.ravuri.calibration.service.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LogManager.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    @Transactional
    public Role createRole(Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new RuntimeException("Role with name " + role.getName() + " already exists");
        }
        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public Role updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // Check if new name conflicts with existing role (excluding current role)
        Optional<Role> existingRole = roleRepository.findByName(roleDetails.getName());
        if (existingRole.isPresent() && !existingRole.get().getId().equals(id)) {
            throw new RuntimeException("Role with name " + roleDetails.getName() + " already exists");
        }

        role.setName(roleDetails.getName());
        role.setDescription(roleDetails.getDescription());

        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        if (!role.getUsers().isEmpty()) {
            throw new RuntimeException("Cannot delete role as it is assigned to users");
        }

        roleRepository.deleteById(id);
    }
}
