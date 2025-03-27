package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    // Buscar roles de un usuario específico por su ID
    List<UserRole> findByUser_Id(Long userId);

    // Buscar usuarios por un rol específico por su ID
    List<UserRole> findByRole_Id(Long roleId);

    // Contar cuántos roles tiene un usuario
    long countByUser_Id(Long userId);
}