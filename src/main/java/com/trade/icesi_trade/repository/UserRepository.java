package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar usuario por ID
    Optional<User> findById(Long id);  
    // Buscar usuario por email (único)
    User findByEmail(String email);
    // Buscar usuarios por nombre (parcial)
    List<User> findByNameContaining(String name);
}
