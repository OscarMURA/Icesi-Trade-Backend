package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
            Role findByRole(String role);
            Role findByRole_Id(Long role_id);
}