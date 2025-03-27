package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar usuario por ID
    User findByUser_Id(Long id);

    // Buscar usuario por email (único)
    User findByEmail(String email);

    // Buscar usuarios por nombre (parcial)
    List<User> findByNameContaining(String name);
}
