package com.trade.icesi_trade.mappers;

import com.trade.icesi_trade.dtos.ReviewDto;
import com.trade.icesi_trade.model.Review;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "reviewer.id", target = "reviewerId")
    @Mapping(source = "reviewee.id", target = "revieweeId")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    ReviewDto entityToDto(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "reviewer", ignore = true)
    @Mapping(target = "reviewee", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Review dtoToEntity(ReviewDto dto);
}