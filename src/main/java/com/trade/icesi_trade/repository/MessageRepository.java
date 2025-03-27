package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySender_Id(Long senderId);  
    List<Message> findByReceiver_Id(Long receiverId);  
}