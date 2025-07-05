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
    public ResponseEntity<List<ChatMessageDto>> getMessages(@RequestParam("userId") Long userId) {
        List<ChatMessage> messages = chatMessageService.getMessagesByUser(userId);
        List<ChatMessageDto> messageDtos = messages.stream()
                .map(messageMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageDtos);
    }

    @MessageMapping("/chat.private")
    public void handlePrivateMessage(ChatMessageDto messageDto) {
        try {
            System.out.println("Recibido mensaje: " + messageDto.getContent() + " de " + messageDto.getSenderId()
                    + " a " + messageDto.getReceiverId());

            // Crear mensaje con datos mínimos para envío rápido
            ChatMessage message = messageMapper.dtoToEntity(messageDto);
            message.setSender(userService.findUserById(messageDto.getSenderId()));
            message.setReceiver(userService.findUserById(messageDto.getReceiverId()));
            message.setCreatedAt(LocalDateTime.now());

            // Enviar mensaje inmediatamente al destinatario (antes de guardar en BD)
            ChatMessageDto immediateMessageDto = messageMapper.entityToDto(message);
            immediateMessageDto.setId(null); // Indicar que es un mensaje temporal

            messagingTemplate.convertAndSendToUser(
                    message.getReceiver().getName(),
                    "/queue/messages",
                    immediateMessageDto);

            // Guardar en BD de forma asíncrona
            ChatMessage savedMessage = chatMessageService.saveMessage(message);
            ChatMessageDto savedMessageDto = messageMapper.entityToDto(savedMessage);

            // Enviar confirmación con ID real al remitente
            messagingTemplate.convertAndSendToUser(
                    message.getSender().getName(),
                    "/queue/messages",
                    savedMessageDto);

            System.out.println("Mensaje procesado y enviado exitosamente");

        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PostMapping
    public ResponseEntity<ChatMessageDto> createMessage(@RequestBody ChatMessageDto messageDto) {
        ChatMessage message = messageMapper.dtoToEntity(messageDto);
        message.setSender(userService.findUserById(messageDto.getSenderId()));
        message.setReceiver(userService.findUserById(messageDto.getReceiverId()));

        ChatMessage savedMessage = chatMessageService.saveMessage(message);
        ChatMessageDto savedMessageDto = messageMapper.entityToDto(savedMessage);
        return ResponseEntity.status(201).body(savedMessageDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        boolean deleted = chatMessageService.deleteMessage(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
