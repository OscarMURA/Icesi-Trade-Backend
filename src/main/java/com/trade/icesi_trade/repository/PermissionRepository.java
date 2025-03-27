package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByName(String name);
    List<Permission> findAll();
    Permission findByPermission_Id(Long id);
}
