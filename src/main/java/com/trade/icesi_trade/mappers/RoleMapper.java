package com.trade.icesi_trade.mappers;

import com.trade.icesi_trade.dtos.RoleDto;
import com.trade.icesi_trade.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto entityToDto(Role role);

    Role dtoToEntity(RoleDto dto);
}