package com.trade.icesi_trade.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {
    private String email;
    private String token;
    private List<String> roles;
}