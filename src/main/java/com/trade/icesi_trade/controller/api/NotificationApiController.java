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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final UserService userService;
    private final NotificationTypeService notificationTypeService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAll() {
        List<NotificationDto> list = notificationService.getAllNotifications()
                .stream()
                .map(notificationMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getById(@PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(notificationMapper::entityToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NotificationDto> create(@RequestBody NotificationDto dto) {
        Notification notification = notificationMapper.dtoToEntity(dto);
        notification.setUser(userService.findUserById(dto.getUserId()));
        notification.setType(notificationTypeService.findById(dto.getTypeId()));
        notification.setCreatedAt(LocalDateTime.now());
        Notification saved = notificationService.createNotification(notification);
        return ResponseEntity.status(201).body(notificationMapper.entityToDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationDto> update(@PathVariable Long id, @RequestBody NotificationDto dto) {
        Notification notification = notificationMapper.dtoToEntity(dto);
        notification.setId(id);
        notification.setUser(userService.findUserById(dto.getUserId()));
        notification.setType(notificationTypeService.findById(dto.getTypeId()));
        notification.setCreatedAt(LocalDateTime.now());
        Notification updated = notificationService.createNotification(notification);
        return ResponseEntity.ok(notificationMapper.entityToDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
