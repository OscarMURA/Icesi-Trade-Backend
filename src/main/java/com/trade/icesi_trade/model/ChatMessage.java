package com.trade.icesi_trade.model;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único del mensaje

    @Column(nullable = false)
    private String content; // Contenido del mensaje

    @Column(nullable = false)
    private LocalDateTime createdAt; // Fecha y hora en que se envió el mensaje

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // Relación con el usuario que envía el mensaje

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver; // Relación con el usuario que recibe el mensaje
}
