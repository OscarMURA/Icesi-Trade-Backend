package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    // Buscar permiso por nombre
    Permission findByName(String name);

    // Buscar todos los permisos
    List<Permission> findAll();

    // Buscar permiso por ID
    Permission findByPermission_Id(Long id);
}
