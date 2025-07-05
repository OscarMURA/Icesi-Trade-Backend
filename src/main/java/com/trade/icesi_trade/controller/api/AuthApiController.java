package com.trade.icesi_trade.controller.api;

import com.trade.icesi_trade.Service.Impl.JwtServiceImpl;
import com.trade.icesi_trade.Service.Interface.UserService;
import com.trade.icesi_trade.Service.Interface.EmailService;
import com.trade.icesi_trade.dtos.LogInDto;
import com.trade.icesi_trade.dtos.RegisterDto;
import com.trade.icesi_trade.dtos.TokenDto;
import com.trade.icesi_trade.mappers.UserMapper;
import com.trade.icesi_trade.model.User;
import com.trade.icesi_trade.model.EmailVerification;
import com.trade.icesi_trade.repository.EmailVerificationRepository;
import com.trade.icesi_trade.repository.UserRepository;

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
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private UserRepository userRepository;

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

            // Obtener el usuario completo desde la base de datos para obtener el nombre
            // real
            User user = userService.findUserByEmail(loginDto.getEmail());
            String username = user.getName(); // Usar el nombre real del usuario
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

    @Operation(summary = "Verify email with token")
    @PostMapping(value = "/verify-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        try {
            System.out.println("Verificando token: " + token);

            // Buscar la verificación por token
            var verificationOpt = emailVerificationRepository.findByToken(token);

            if (verificationOpt.isEmpty()) {
                System.out.println("Token no encontrado: " + token);
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\": \"Token inválido\"}");
            }

            EmailVerification verification = verificationOpt.get();
            System.out.println("Token encontrado para email: " + verification.getEmail());
            System.out.println("Estado del token - Usado: " + verification.isUsed() + ", Expira: "
                    + verification.getExpiresAt());

            // Verificar que no haya sido usado y no haya expirado
            if (verification.isUsed()) {
                System.out.println("Token ya utilizado: " + token);
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\": \"Este enlace de verificación ya ha sido utilizado. Tu cuenta ya está verificada y puedes iniciar sesión.\"}");
            }

            if (verification.getExpiresAt().isBefore(java.time.LocalDateTime.now())) {
                System.out.println("Token expirado: " + token);
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\": \"Token ha expirado\"}");
            }

            // Habilitar el usuario
            User user = userRepository.findByEmail(verification.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            System.out.println("👤 Usuario encontrado: " + user.getEmail() + " (Habilitado: " + user.isEnabled() + ")");

            // Si el usuario ya está habilitado, solo marcar el token como usado
            if (user.isEnabled()) {
                System.out.println("Usuario ya está habilitado, solo marcando token como usado");
                verification.setUsed(true);
                emailVerificationRepository.save(verification);
                System.out.println("Token marcado como usado: " + token);

                return ResponseEntity.ok(Map.of(
                        "message", "Tu cuenta ya está verificada y puedes iniciar sesión",
                        "verified", true,
                        "userEmail", user.getEmail()));
            }

            user.setEnabled(true);
            userRepository.save(user);
            System.out.println("Usuario habilitado: " + user.getEmail());

            // Marcar token como usado
            verification.setUsed(true);
            emailVerificationRepository.save(verification);
            System.out.println("Token marcado como usado: " + token);

            // Enviar email de bienvenida
            try {
                emailService.sendWelcomeEmail(user.getEmail(), user.getName());
                System.out.println("Email de bienvenida enviado a: " + user.getEmail());
            } catch (Exception e) {
                // No fallar si el email de bienvenida falla
                System.err.println("Error enviando email de bienvenida: " + e.getMessage());
            }

            System.out.println("🎉 Verificación completada exitosamente para: " + user.getEmail());
            return ResponseEntity.ok(Map.of(
                    "message", "Email verificado exitosamente",
                    "verified", true,
                    "userEmail", user.getEmail()));

        } catch (Exception e) {
            System.err.println("Error en verificación: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"Error verificando email: " + e.getMessage() + "\"}");
        }
    }

    @Operation(summary = "Resend verification email")
    @PostMapping(value = "/resend-verification", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<?> resendVerification(@RequestParam String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (user.isEnabled()) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\": \"El email ya está verificado\"}");
            }

            // Eliminar verificaciones anteriores para este email
            emailVerificationRepository.deleteByEmailAndType(email,
                    EmailVerification.VerificationType.EMAIL_VERIFICATION);

            // Generar nuevo token
            String newToken = java.util.UUID.randomUUID().toString().replace("-", "");

            EmailVerification verification = EmailVerification.builder()
                    .token(newToken)
                    .email(email)
                    .expiresAt(java.time.LocalDateTime.now().plusHours(24))
                    .type(EmailVerification.VerificationType.EMAIL_VERIFICATION)
                    .used(false)
                    .build();

            emailVerificationRepository.save(verification);

            // Enviar nuevo email de verificación
            emailService.sendVerificationEmail(email, newToken, user.getName());

            return ResponseEntity.ok(Map.of(
                    "message", "Email de verificación reenviado exitosamente",
                    "email", email));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"Error reenviando verificación: " + e.getMessage() + "\"}");
        }
    }
}
