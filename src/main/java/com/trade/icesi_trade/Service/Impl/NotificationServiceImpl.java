package com.trade.icesi_trade.Service.Impl;

import com.trade.icesi_trade.Service.Interface.NotificationService;
import com.trade.icesi_trade.model.Notification;
import com.trade.icesi_trade.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Creates a new notification and saves it to the repository.
     * 
     * @param notification The notification object to be created. Must not be null.
     *                     If the creation timestamp is not provided, it will be set
     *                     to the current date and time. If the read status is not
     *                     specified, it will default to false (unread).
     * @return The saved notification object.
     * @throws IllegalArgumentException If the provided notification is null.
     */
    @Override
    public Notification createNotification(Notification notification) {
        if (notification == null) {
            throw new IllegalArgumentException("La notificación no puede ser nula.");
        }
        if (notification.getUser() == null) {
            throw new IllegalArgumentException("La notificación debe tener un usuario asociado.");
        }
        if (notification.getType() == null) {
            throw new IllegalArgumentException("La notificación debe tener un tipo asociado.");
        }
        if (!StringUtils.hasText(notification.getMessage())) {
            throw new IllegalArgumentException("La notificación debe tener un mensaje.");
        }
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }
        if (notification.getRead() == null) {
            notification.setRead(false);
        }
        return notificationRepository.save(notification);
    }

    /**
     * Marks a notification as read by its ID.
     *
     * @param notificationId the ID of the notification to be marked as read.
     * @return the updated Notification object after being marked as read.
     * @throws IllegalArgumentException if the notificationId is null.
     * @throws NoSuchElementException   if no notification is found with the given
     *                                  ID.
     */
    @Override
    @Transactional
    public Notification markAsRead(Long notificationId) {
        if (notificationId == null) {
            throw new IllegalArgumentException("El ID de la notificación no puede ser nulo.");
        }
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(
                        () -> new NoSuchElementException("Notificación no encontrada con el ID: " + notificationId));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    /**
     * Retrieves a list of notifications associated with a specific user.
     *
     * @param userId the ID of the user whose notifications are to be retrieved.
     *               Must not be null.
     * @return a list of {@link Notification} objects associated with the specified
     *         user.
     * @throws IllegalArgumentException if the provided userId is null.
     */
    @Override
    public List<Notification> getNotificationsByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo.");
        }
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Retrieves a list of pending notifications for a specific user.
     * A notification is considered pending if it has not been read
     * (i.e., its "read" property is null or false).
     *
     * @param userId the ID of the user whose pending notifications are to be
     *               retrieved
     * @return a list of pending notifications for the specified user
     */
    @Override
    public List<Notification> getPendingNotificationsByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo.");
        }
        return notificationRepository.findByUserIdAndReadFalse(userId);
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public void deleteNotification(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la notificación no puede ser nulo.");
        }
        if (!notificationRepository.existsById(id)) {
            throw new NoSuchElementException("Notificación no encontrada con el ID: " + id);
        }
        notificationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo.");
        }
        List<Notification> notifications = getPendingNotificationsByUser(userId);
        notifications.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}
