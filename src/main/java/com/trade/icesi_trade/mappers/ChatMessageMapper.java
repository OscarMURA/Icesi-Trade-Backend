package com.trade.icesi_trade.mappers;

import com.trade.icesi_trade.dtos.ChatMessageDto;
import com.trade.icesi_trade.model.ChatMessage;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    ChatMessageDto entityToDto(ChatMessage chatMessage);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ChatMessage dtoToEntity(ChatMessageDto chatMessageDto);
}
