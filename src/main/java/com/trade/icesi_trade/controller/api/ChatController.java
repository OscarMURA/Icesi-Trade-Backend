package com.trade.icesi_trade.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trade.icesi_trade.Service.Interface.ChatMessageService;
import com.trade.icesi_trade.Service.Interface.UserService;
import com.trade.icesi_trade.dtos.UserResponseDto;
import com.trade.icesi_trade.mappers.UserMapper;
import com.trade.icesi_trade.model.User;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        try {
            List<UserResponseDto> users = userService.findAllUsers().stream()
                    .map(userMapper::entityToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<UserResponseDto>> getChatContacts(@RequestParam Long userId) {
        List<User> contacts = chatMessageService.getChatContacts(userId);
        List<UserResponseDto> result = contacts.stream()
                .map(userMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}