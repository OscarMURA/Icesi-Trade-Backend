package com.trade.icesi_trade.controller.api;

import com.trade.icesi_trade.Service.Impl.JwtServiceImpl;
import com.trade.icesi_trade.dtos.LogInDto;
import com.trade.icesi_trade.dtos.TokenDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthApiController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtServiceImpl jwtService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LogInDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        String token = jwtService.generateToken(authentication);

        List<String> roles = authentication.getAuthorities()
            .stream()
            .map(a -> a.getAuthority())
            .toList();

        TokenDto tokenDto = new TokenDto(loginDto.getEmail(), token, roles);
        return ResponseEntity.ok(tokenDto);
    }
}
