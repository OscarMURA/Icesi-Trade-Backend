package com.trade.icesi_trade.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SaleDto {
    private Long id;
    private Long buyerId;
    private Long productId;
    private Double price;
    private String status;
    private LocalDateTime createdAt;
}
