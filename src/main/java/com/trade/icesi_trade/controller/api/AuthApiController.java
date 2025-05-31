package com.trade.icesi_trade.controller.api;

import com.trade.icesi_trade.Service.Impl.JwtServiceImpl;
import com.trade.icesi_trade.dtos.LogInDto;
import com.trade.icesi_trade.dtos.TokenDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
@Tag(name = "Authentication", description = "Authentication operations")
public class AuthApiController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtServiceImpl jwtService;

    @Operation(summary = "User login")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LogInDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

            String token = jwtService.generateToken(authentication);

            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(a -> a.getAuthority())
                    .toList();

            String username = authentication.getName();
            long creationTime = jwtService.getTokenCreationTime(token);
            long expirationTime = jwtService.getTokenExpirationTime(token);

            TokenDto tokenDto = new TokenDto(username, loginDto.getEmail(), roles, token, creationTime, expirationTime);
            return ResponseEntity.ok(tokenDto);
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"Credenciales inválidas\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"Error en el servidor: " + e.getMessage() + "\"}");
        }
    }
}
