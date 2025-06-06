package com.trade.icesi_trade.controller.api;

import com.trade.icesi_trade.dtos.ChatMessageDto;
import com.trade.icesi_trade.Service.Impl.ChatMessageServiceImpl;
import com.trade.icesi_trade.Service.Interface.UserService;
import com.trade.icesi_trade.mappers.ChatMessageMapper;
import com.trade.icesi_trade.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat/messages")
public class ChatMessageController {

    @Autowired
    private ChatMessageServiceImpl chatMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatMessageMapper messageMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<ChatMessageDto>> getMessages(@RequestParam("userId") Long userId) {
        List<ChatMessage> messages = chatMessageService.getMessagesByUser(userId);
        List<ChatMessageDto> messageDtos = messages.stream()
                .map(messageMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageDtos);
    }

    @MessageMapping("/chat.private")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void handlePrivateMessage(ChatMessageDto messageDto) {
        ChatMessage message = messageMapper.dtoToEntity(messageDto);
        message.setSender(userService.findUserById(messageDto.getSenderId()));
        message.setReceiver(userService.findUserById(messageDto.getReceiverId()));
        message.setCreatedAt(LocalDateTime.now());

        ChatMessage savedMessage = chatMessageService.saveMessage(message);
        ChatMessageDto savedMessageDto = messageMapper.entityToDto(savedMessage);

        // Enviar mensaje al destinatario
        messagingTemplate.convertAndSendToUser(
                message.getReceiver().getName(),
                "/queue/messages",
                savedMessageDto);

        // Enviar mensaje al remitente (para confirmación)
        messagingTemplate.convertAndSendToUser(
                message.getSender().getName(),
                "/queue/messages",
                savedMessageDto);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ChatMessageDto> createMessage(@RequestBody ChatMessageDto messageDto) {
        ChatMessage message = messageMapper.dtoToEntity(messageDto);
        message.setSender(userService.findUserById(messageDto.getSenderId()));
        message.setReceiver(userService.findUserById(messageDto.getReceiverId()));

        ChatMessage savedMessage = chatMessageService.saveMessage(message);
        ChatMessageDto savedMessageDto = messageMapper.entityToDto(savedMessage);
        return ResponseEntity.status(201).body(savedMessageDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        boolean deleted = chatMessageService.deleteMessage(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
