package com.trade.icesi_trade.controller.api;

import com.trade.icesi_trade.Service.Impl.JwtServiceImpl;
import com.trade.icesi_trade.Service.Interface.UserService;
import com.trade.icesi_trade.dtos.LogInDto;
import com.trade.icesi_trade.dtos.RegisterDto;
import com.trade.icesi_trade.dtos.TokenDto;
import com.trade.icesi_trade.mappers.UserMapper;
import com.trade.icesi_trade.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "${app.frontend.url}", "${app.frontend.alternative-url}" })
@Tag(name = "Authentication", description = "Authentication operations")
public class AuthApiController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

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

    @Operation(summary = "Register new user")
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto dto) {
        try {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\": \"Las contraseñas no coinciden\"}");
            }

            User createdUser = userService.register(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userMapper.entityToDto(createdUser));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"Error en el servidor: " + e.getMessage() + "\"}");
        }
    }

    @Operation(summary = "Get user role and redirect information")
    @GetMapping(value = "/role-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRoleInfo(Authentication auth) {
        try {
            String role = auth.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");

            String redirectPath = role.equals("ROLE_ADMIN") ? "/users" : "/home";

            return ResponseEntity.ok(Map.of(
                    "role", role,
                    "redirectPath", redirectPath));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"Error al obtener información del rol: " + e.getMessage() + "\"}");
        }
    }
}
