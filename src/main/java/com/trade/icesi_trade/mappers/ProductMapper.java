package com.trade.icesi_trade.mappers;

import com.trade.icesi_trade.dtos.ProductDto;
import com.trade.icesi_trade.model.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mappings({
        @Mapping(source = "category.id", target = "categoryId"),
        @Mapping(source = "seller.id", target = "sellerId")
    })
    ProductDto entityToDto(Product product);

    @InheritInverseConfiguration
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product dtoToEntity(ProductDto dto);
}
