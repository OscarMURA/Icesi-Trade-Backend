package com.trade.icesi_trade.Service.Impl;

import java.util.List;
import java.util.Optional;

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
        if (name == null) {
            throw new IllegalArgumentException("El nombre del rol no puede ser nulo.");
        }
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
        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException("El rol no existe.");
        }
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role updateRole(Long roleId, Role updatedRole) {
        if (roleId == null || updatedRole == null) {
            throw new IllegalArgumentException("El ID del rol y el rol actualizado no pueden ser nulos.");
        }

        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException("El rol no existe.");
        }

        if (rolePermissionRepository.findByRole_Id(roleId).isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener al menos un permiso asignado.");
        }

        return roleRepository.save(updatedRole);
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findAllById(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("La lista de IDs no puede ser nula o vacía.");
        }
        return roleRepository.findAllById(ids);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }
}
