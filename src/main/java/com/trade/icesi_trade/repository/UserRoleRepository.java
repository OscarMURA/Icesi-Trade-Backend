package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
}