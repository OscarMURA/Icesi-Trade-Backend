package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}