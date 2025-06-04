package com.trade.icesi_trade.Service.Impl;

import com.trade.icesi_trade.Service.Interface.ChatMessageService;
import com.trade.icesi_trade.dtos.ChatMessageDto;
import com.trade.icesi_trade.mappers.ChatMessageMapper;
import com.trade.icesi_trade.model.ChatMessage;
import com.trade.icesi_trade.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    public void saveMessage(ChatMessageDto messageDto) {
        ChatMessage message = chatMessageMapper.dtoToEntity(messageDto);
        chatMessageRepository.save(message);
    }

    public List<ChatMessageDto> getMessages(Long userId) {
        List<ChatMessage> messages = chatMessageRepository.findBySenderIdOrReceiverId(userId, userId);
        return messages.stream()
                .map(chatMessageMapper::entityToDto)
                .toList();
    }
}
