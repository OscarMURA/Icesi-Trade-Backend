package com.trade.icesi_trade.Service.Impl;

import com.trade.icesi_trade.Service.Interface.ChatMessageService;
import com.trade.icesi_trade.model.ChatMessage;
import com.trade.icesi_trade.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import com.trade.icesi_trade.model.User;

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

    @Override
    public List<User> getChatContacts(Long userId) {
        List<ChatMessage> messages = chatMessageRepository.findBySenderIdOrReceiverId(userId, userId);
        Set<User> contacts = new HashSet<>();
        for (ChatMessage msg : messages) {
            if (!msg.getSender().getId().equals(userId)) {
                contacts.add(msg.getSender());
            }
            if (!msg.getReceiver().getId().equals(userId)) {
                contacts.add(msg.getReceiver());
            }
        }
        return new ArrayList<>(contacts);
    }
}
