package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
            Message findByUser_Id(Long user_id);
            Message findByMessage_Id(Long message_id);
}