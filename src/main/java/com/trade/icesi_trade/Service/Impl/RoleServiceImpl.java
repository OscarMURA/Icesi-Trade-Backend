package com.trade.icesi_trade.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trade.icesi_trade.Service.Interface.RoleService;
import com.trade.icesi_trade.model.Role;
import com.trade.icesi_trade.repository.RolePermissionRepository;
import com.trade.icesi_trade.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private  RoleRepository roleRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    
    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role saveRole(Role role) {
        if (role.getId() == null || role.getName() == null) {
            throw new IllegalArgumentException("El rol debe tener al menos un ID y un nombre.");
        }

        // Validar que el rol tenga al menos un permiso asociado
        if (rolePermissionRepository.findByRole_Id(role.getId()).isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener al menos un permiso asignado.");
        }

        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }
}
