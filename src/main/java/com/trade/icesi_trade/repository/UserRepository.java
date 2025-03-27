package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUser_Id(Long id);
    User findByEmail(String email);
    List<User> findByNameContaining(String name);
}
