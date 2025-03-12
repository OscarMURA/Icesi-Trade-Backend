package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}