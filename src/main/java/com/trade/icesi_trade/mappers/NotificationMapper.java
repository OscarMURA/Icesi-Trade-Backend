package com.trade.icesi_trade.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.trade.icesi_trade.dtos.NotificationDto;
import com.trade.icesi_trade.model.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    NotificationDto entityToDto(Notification entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Notification dtoToEntity(NotificationDto dto);
}