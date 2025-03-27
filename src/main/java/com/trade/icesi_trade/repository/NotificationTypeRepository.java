package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.NotificationType;

import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {

            NotificationType findByNotificationType_Id(Long notificationType_id);
            NotificationType findByName(VarcharJdbcType name);
}