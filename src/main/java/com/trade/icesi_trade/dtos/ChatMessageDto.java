package com.trade.icesi_trade.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String createdAt;
}
