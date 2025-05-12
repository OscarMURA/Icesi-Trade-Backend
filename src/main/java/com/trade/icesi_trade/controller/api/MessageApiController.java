package com.trade.icesi_trade.controller.api;

import com.trade.icesi_trade.Service.Interface.MessageService;
import com.trade.icesi_trade.Service.Interface.UserService;
import com.trade.icesi_trade.dtos.MessageDto;
import com.trade.icesi_trade.mappers.MessageMapper;
import com.trade.icesi_trade.model.Message;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageApiController {
    @Autowired
    private final MessageService messageService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final MessageMapper messageMapper;

    @GetMapping
    public ResponseEntity<List<MessageDto>> getAllMessages() {
        List<MessageDto> messages = messageService.getAllMessages().stream()
                .map(messageMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDto> getMessageById(@PathVariable Long id) {
        return messageService.getMessageById(id).stream()
                .findFirst()
                .map(messageMapper::entityToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MessageDto> createMessage(@RequestBody MessageDto dto) {
        Message message = messageMapper.dtoToEntity(dto);
        message.setSender(userService.findUserById(dto.getSenderId()));
        message.setReceiver(userService.findUserById(dto.getReceiverId()));
        message.setCreatedAt(LocalDateTime.now());

        Message saved = messageService.sendMessage(message);
        return ResponseEntity.status(201).body(messageMapper.entityToDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageDto> updateMessage(@PathVariable Long id, @RequestBody MessageDto dto) {
        Message message = messageMapper.dtoToEntity(dto);
        message.setId(id);
        message.setSender(userService.findUserById(dto.getSenderId()));
        message.setReceiver(userService.findUserById(dto.getReceiverId()));
        message.setCreatedAt(LocalDateTime.now());

        Message updated = messageService.sendMessage(message);
        return ResponseEntity.ok(messageMapper.entityToDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}