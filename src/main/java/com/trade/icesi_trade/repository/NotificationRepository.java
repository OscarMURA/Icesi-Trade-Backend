package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}