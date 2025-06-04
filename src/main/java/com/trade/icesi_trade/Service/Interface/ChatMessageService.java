package com.trade.icesi_trade.Service.Interface;

import com.trade.icesi_trade.model.ChatMessage;
import java.util.List;

public interface ChatMessageService {
    ChatMessage saveMessage(ChatMessage message);

    List<ChatMessage> getMessagesByUser(Long userId);

    boolean deleteMessage(Long id);
}