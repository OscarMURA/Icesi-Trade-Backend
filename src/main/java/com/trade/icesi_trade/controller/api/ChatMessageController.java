package com.trade.icesi_trade.controller.api;

import com.trade.icesi_trade.dtos.ChatMessageDto;
import com.trade.icesi_trade.Service.Impl.ChatMessageServiceImpl;
import com.trade.icesi_trade.Service.Interface.UserService;
import com.trade.icesi_trade.mappers.ChatMessageMapper;
import com.trade.icesi_trade.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/messages") // Ruta base para el chat
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
    public ResponseEntity<List<ChatMessageDto>> getMessages(@RequestParam("userId") Long userId) {
        List<Message> messages = chatMessageService.getMessagesByUser(userId); 
        List<ChatMessageDto> messageDtos = messageMapper.entitiesToDtos(messages); 
        return ResponseEntity.ok(messageDtos); 
    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(ChatMessageDto messageDto) {
        // Convertimos el DTO a entidad
        Message message = messageMapper.dtoToEntity(messageDto);

        // Establecemos el sender y receiver usando el ID de los usuarios
        message.setSender(userService.findUserById(messageDto.getSenderId()));
        message.setReceiver(userService.findUserById(messageDto.getReceiverId()));

        // Guardamos el mensaje en la base de datos
        chatMessageService.saveMessage(message);

        // Enviar el mensaje a los usuarios conectados a través de WebSocket
        messagingTemplate.convertAndSend("/topic/" + message.getReceiver().getUsername(), messageDto);
    }

    // Crear un nuevo mensaje
    @PostMapping
    public ResponseEntity<ChatMessageDto> createMessage(@RequestBody ChatMessageDto messageDto) {
        // Convertimos el DTO a entidad
        Message message = messageMapper.dtoToEntity(messageDto);
        
        // Establecemos el sender y receiver usando el ID de los usuarios
        message.setSender(userService.findUserById(messageDto.getSenderId()));
        message.setReceiver(userService.findUserById(messageDto.getReceiverId()));
        
        Message savedMessage = chatMessageService.saveMessage(message);

        ChatMessageDto savedMessageDto = messageMapper.entityToDto(savedMessage);
        return ResponseEntity.status(201).body(savedMessageDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        boolean deleted = chatMessageService.deleteMessage(id); // Intentamos eliminar el mensaje
        if (deleted) {
            return ResponseEntity.noContent().build(); // Si se eliminó, respondemos con 204
        } else {
            return ResponseEntity.notFound().build(); // Si no se encontró, respondemos con 404
        }
    }
}
