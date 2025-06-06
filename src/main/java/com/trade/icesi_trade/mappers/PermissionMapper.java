package com.trade.icesi_trade.mappers;

import com.trade.icesi_trade.dtos.PermissionDto;
import com.trade.icesi_trade.model.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionDto entityToDto(Permission permission);

    Permission dtoToEntity(PermissionDto dto);
}