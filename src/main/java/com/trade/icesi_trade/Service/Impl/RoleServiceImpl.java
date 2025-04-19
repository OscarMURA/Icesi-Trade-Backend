package com.trade.icesi_trade.Service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trade.icesi_trade.Service.Interface.RoleService;
import com.trade.icesi_trade.model.Role;
import com.trade.icesi_trade.repository.RolePermissionRepository;
import com.trade.icesi_trade.model.Permission;
import com.trade.icesi_trade.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private  RoleRepository roleRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    
    /**
     * Finds a role by its name.
     *
     * @param name the name of the role to find; must not be null.
     * @return the Role object corresponding to the given name.
     * @throws IllegalArgumentException if the provided name is null.
     */
    @Override
    public Role findRoleByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("El nombre del rol no puede ser nulo.");
        }
        return roleRepository.findByName(name);
    }
    

    /**
     * Saves a role to the repository after performing necessary validations.
     *
     * @param role The {@link Role} object to be saved. It must have a non-null ID and name.
     * @return The saved {@link Role} object.
     * @throws IllegalArgumentException If the role does not have an ID, a name, 
     *                                  or at least one associated permission.
     */
    
    @Transactional
    @Override
    public Role saveRole(Role role, List<Permission> permissions) {
        if (role.getName() == null || role.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio.");
        }

        if (permissions == null || permissions.isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener al menos un permiso asignado.");
        }

        // Guardar el rol
        Role savedRole = roleRepository.save(role);

        // Asignar permisos
        for (Permission permission : permissions) {
            rolePermissionRepository.save(
                com.trade.icesi_trade.model.RolePermission.builder()
                    .id(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE)
                    .role(savedRole)
                    .permission(permission)
                    .build()
            );
        }

        return savedRole;
    }

    

    /**
     * Deletes a role by its ID.
     *
     * @param roleId the ID of the role to be deleted
     * @throws IllegalArgumentException if the role with the specified ID does not exist
     */
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
