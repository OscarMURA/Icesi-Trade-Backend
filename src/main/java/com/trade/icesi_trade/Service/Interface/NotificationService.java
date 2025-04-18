package com.trade.icesi_trade.Service.Interface;

import com.trade.icesi_trade.model.Notification;
import java.util.List;

public interface NotificationService {
    Notification createNotification(Notification notification);
    
    Notification markNotificationAsRead(Long notificationId);
    
    List<Notification> getNotificationsByUser(Long userId);
    
    List<Notification> getPendingNotificationsByUser(Long userId);
}
