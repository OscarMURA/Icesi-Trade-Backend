package com.trade.icesi_trade.controller.api;

import com.trade.icesi_trade.Service.Interface.NotificationService;
import com.trade.icesi_trade.Service.Interface.UserService;
import com.trade.icesi_trade.Service.Interface.NotificationTypeService;
import com.trade.icesi_trade.dtos.NotificationDto;
import com.trade.icesi_trade.mappers.NotificationMapper;
import com.trade.icesi_trade.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "CRUD operations for notifications")
public class NotificationApiController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final UserService userService;
    private final NotificationTypeService notificationTypeService;

    @Operation(summary = "Get all notifications")
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<NotificationDto>> getAll(@RequestParam(required = false) String userId) {
        if (userId != null) {
            List<NotificationDto> userNotifications = notificationService.getNotificationsByUser(Long.parseLong(userId))
                    .stream()
                    .map(notificationMapper::entityToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userNotifications);
        }
        List<NotificationDto> list = notificationService.getAllNotifications()
                .stream()
                .map(notificationMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Get notification by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<NotificationDto> getById(@PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(notificationMapper::entityToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new notification")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<NotificationDto> create(@RequestBody NotificationDto dto) {
        Notification notification = notificationMapper.dtoToEntity(dto);
        notification.setUser(userService.findUserById(dto.getUserId()));
        notification.setType(notificationTypeService.findById(dto.getTypeId()));
        notification.setCreatedAt(LocalDateTime.now());
        Notification saved = notificationService.createNotification(notification);
        return ResponseEntity.status(201).body(notificationMapper.entityToDto(saved));
    }

    @Operation(summary = "Update notification by ID")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<NotificationDto> update(@PathVariable Long id, @RequestBody NotificationDto dto) {
        Notification notification = notificationMapper.dtoToEntity(dto);
        notification.setId(id);
        notification.setUser(userService.findUserById(dto.getUserId()));
        notification.setType(notificationTypeService.findById(dto.getTypeId()));
        notification.setCreatedAt(LocalDateTime.now());
        Notification updated = notificationService.createNotification(notification);
        return ResponseEntity.ok(notificationMapper.entityToDto(updated));
    }

    @Operation(summary = "Delete notification by ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get notifications by user ID")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<NotificationDto>> getByUserId(@PathVariable Long userId) {
        List<NotificationDto> list = notificationService.getNotificationsByUser(userId)
                .stream()
                .map(notificationMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Get pending notifications by user ID")
    @GetMapping("/user/{userId}/pending")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<NotificationDto>> getPendingByUserId(@PathVariable Long userId) {
        List<NotificationDto> list = notificationService.getPendingNotificationsByUser(userId)
                .stream()
                .map(notificationMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Mark notification as read")
    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable Long id) {
        Notification notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notificationMapper.entityToDto(notification));
    }

    @Operation(summary = "Mark all notifications as read for a user")
    @PutMapping("/user/{userId}/read-all")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
}
