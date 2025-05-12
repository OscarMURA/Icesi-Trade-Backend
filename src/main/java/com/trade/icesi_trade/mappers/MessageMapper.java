package com.trade.icesi_trade.mappers;

import com.trade.icesi_trade.dtos.MessageDto;
import com.trade.icesi_trade.model.Message;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    MessageDto entityToDto(Message message);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Message dtoToEntity(MessageDto dto);
}