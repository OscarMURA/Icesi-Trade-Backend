package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
}