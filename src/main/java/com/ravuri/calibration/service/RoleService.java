package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    public List<Role> getAllRoles();
    public Optional<Role> getRoleById(Long id);
    public Optional<Role> getRoleByName(String name);
    public Role createRole(Role role);
    public Role updateRole(Long id, Role roleDetails);
    public void deleteRole(Long id);
}
