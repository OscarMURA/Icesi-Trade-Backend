package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}