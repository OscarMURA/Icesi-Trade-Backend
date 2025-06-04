package com.trade.icesi_trade.Service.Impl;

import com.trade.icesi_trade.Service.Interface.ChatMessageService;
import com.trade.icesi_trade.model.ChatMessage;
import com.trade.icesi_trade.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    @Override
    public List<ChatMessage> getMessagesByUser(Long userId) {
        return chatMessageRepository.findBySenderIdOrReceiverId(userId, userId);
    }

    @Override
    public boolean deleteMessage(Long id) {
        if (chatMessageRepository.existsById(id)) {
            chatMessageRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
