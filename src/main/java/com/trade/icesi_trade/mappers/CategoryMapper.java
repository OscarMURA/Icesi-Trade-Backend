package com.trade.icesi_trade.mappers;

import com.trade.icesi_trade.dtos.CategoryDto;
import com.trade.icesi_trade.model.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto entityToDto(Category category);
    Category dtoToEntity(CategoryDto dto);
}
