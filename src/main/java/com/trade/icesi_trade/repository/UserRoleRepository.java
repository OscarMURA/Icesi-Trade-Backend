package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUser_Id(Long userId);
    List<UserRole> findByRole_Id(Long roleId);
    long countByUser_Id(Long userId);
}