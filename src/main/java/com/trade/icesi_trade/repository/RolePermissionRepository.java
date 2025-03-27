package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    // Buscar relaciones por role_id
    List<RolePermission> findByRole_Id(Long roleId);

    // Buscar relaciones por permission_id
    List<RolePermission> findByPermission_Id(Long permissionId);

    // Obtener todas las relaciones entre roles y permisos
    List<RolePermission> findAll();

    // Eliminar una relación por role_id y permission_id
    void deleteByRole_IdAndPermission_Id(Long roleId, Long permissionId);
}
