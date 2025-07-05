package com.trade.icesi_trade.Service.Impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.trade.icesi_trade.Service.Interface.EmailService;
import com.trade.icesi_trade.model.EmailVerification;
import com.trade.icesi_trade.model.EmailVerification.VerificationType;
import com.trade.icesi_trade.repository.EmailVerificationRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void sendVerificationEmail(String to, String token, String username) {
        try {
            // Mostrar el token en consola para pruebas
            System.out.println("ENLACE DE VERIFICACIÓN PARA PRUEBAS:");
            System.out.println(frontendUrl + "/verify-email?token=" + token);
            System.out.println("Email: " + to);
            System.out.println("Usuario: " + username);
            System.out.println("Token: " + token);
            System.out.println("==========================================");

            // Intentar enviar el email real
            try {
                System.out.println("Intentando enviar email a: " + to);
                System.out.println("Desde: " + fromEmail);

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromEmail);
                helper.setTo(to);
                helper.setSubject("¡Verifica tu cuenta en Icesi Trade!");

                String htmlContent = createVerificationEmailTemplate(username, token);
                helper.setText(htmlContent, true);

                System.out.println("Mensaje creado, enviando...");
                mailSender.send(message);
                log.info("Email de verificación enviado exitosamente a: {}", to);
                System.out.println("Email enviado correctamente a: " + to);
            } catch (Exception emailError) {
                System.err.println("Error enviando email: " + emailError.getMessage());
                System.err.println("Tipo de error: " + emailError.getClass().getSimpleName());
                emailError.printStackTrace();
                // No fallar el registro si el email falla
            }

        } catch (Exception e) {
            log.error("Error enviando email de verificación a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error enviando email de verificación: " + e.getMessage());
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String token, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Recupera tu contraseña - Icesi Trade");

            String htmlContent = createPasswordResetEmailTemplate(username, token);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email de recuperación de contraseña enviado exitosamente a: {}", to);

        } catch (MessagingException e) {
            log.error("Error enviando email de recuperación a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error enviando email de recuperación: " + e.getMessage());
        }
    }

    @Override
    public void sendWelcomeEmail(String to, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("¡Bienvenido a Icesi Trade!");

            String htmlContent = createWelcomeEmailTemplate(username);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email de bienvenida enviado exitosamente a: {}", to);

        } catch (MessagingException e) {
            log.error("Error enviando email de bienvenida a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error enviando email de bienvenida: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyToken(String token) {
        Optional<EmailVerification> verification = emailVerificationRepository.findByToken(token);

        if (verification.isPresent()) {
            EmailVerification ev = verification.get();

            if (!ev.isUsed() && ev.getExpiresAt().isAfter(LocalDateTime.now())) {
                return true;
            }
        }

        return false;
    }

    private String createVerificationEmailTemplate(String username, String token) {
        String verificationUrl = frontendUrl + "/verify-email?token=" + token;

        return String.format(
                """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <title>Verifica tu cuenta - Icesi Trade</title>
                            <style>
                                body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                                .container { max-width: 600px; margin: 0 auto; background-color: white; }
                                .header { background: linear-gradient(135deg, #6a1b9a 0%%, #3f51b5 100%%); color: white; padding: 30px; text-align: center; }
                                .content { padding: 30px; }
                                .button { background: linear-gradient(135deg, #6a1b9a 0%%, #3f51b5 100%%); color: #fff !important; padding: 15px 30px; text-decoration: none; border-radius: 25px; display: inline-block; font-weight: bold; }
                                .footer { background-color: #f8f9fa; padding: 20px; text-align: center; color: #666; font-size: 12px; }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h1>🎉 ¡Bienvenido a Icesi Trade!</h1>
                                    <p>Tu plataforma de comercio universitario</p>
                                </div>

                                <div class="content">
                                    <h2>Hola %s,</h2>
                                    <p>¡Gracias por registrarte en Icesi Trade! Para completar tu registro y comenzar a usar nuestra plataforma, necesitamos verificar tu dirección de correo electrónico.</p>

                                    <div style="text-align: center; margin: 30px 0;">
                                        <a href="%s" class="button">
                                            ✅ Verificar mi cuenta
                                        </a>
                                    </div>

                                    <p><strong>¿El botón no funciona?</strong></p>
                                    <p>Copia y pega este enlace en tu navegador:</p>
                                    <p style="word-break: break-all; color: #666; background-color: #f8f9fa; padding: 10px; border-radius: 5px;">%s</p>

                                    <div style="background-color: #fff3cd; border: 1px solid #ffeaa7; border-radius: 5px; padding: 15px; margin: 20px 0;">
                                        <p style="margin: 0;"><strong>⚠️ Importante:</strong> Este enlace expira en 24 horas por seguridad.</p>
                                    </div>

                                    <p>Una vez verificado tu email, podrás:</p>
                                    <ul>
                                        <li>Publicar productos para vender</li>
                                        <li>Comprar productos de otros estudiantes</li>
                                        <li>Chatear con otros usuarios</li>
                                        <li>Gestionar tus favoritos</li>
                                    </ul>
                                </div>

                                <div class="footer">
                                    <p>Si no solicitaste esta verificación, puedes ignorar este correo.</p>
                                    <p>© 2024 Icesi Trade - Plataforma de comercio universitario</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                username, verificationUrl, verificationUrl);
    }

    private String createPasswordResetEmailTemplate(String username, String token) {
        String resetUrl = frontendUrl + "/reset-password?token=" + token;

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Recupera tu contraseña - Icesi Trade</title>
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                        .container { max-width: 600px; margin: 0 auto; background-color: white; }
                        .header { background: linear-gradient(135deg, #ff9800 0%, #ffc107 100%); color: white; padding: 30px; text-align: center; }
                        .content { padding: 30px; }
                        .button { background: linear-gradient(135deg, #ff9800 0%, #ffc107 100%); color: white; padding: 15px 30px; text-decoration: none; border-radius: 25px; display: inline-block; font-weight: bold; }
                        .footer { background-color: #f8f9fa; padding: 20px; text-align: center; color: #666; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🔐 Recupera tu contraseña</h1>
                            <p>Icesi Trade - Acceso seguro</p>
                        </div>

                        <div class="content">
                            <h2>Hola %s,</h2>
                            <p>Hemos recibido una solicitud para restablecer tu contraseña en Icesi Trade.</p>

                            <div style="text-align: center; margin: 30px 0;">
                                <a href="%s" class="button">
                                    🔑 Restablecer contraseña
                                </a>
                            </div>

                            <p><strong>¿El botón no funciona?</strong></p>
                            <p>Copia y pega este enlace en tu navegador:</p>
                            <p style="word-break: break-all; color: #666; background-color: #f8f9fa; padding: 10px; border-radius: 5px;">%s</p>

                            <div style="background-color: #fff3cd; border: 1px solid #ffeaa7; border-radius: 5px; padding: 15px; margin: 20px 0;">
                                <p style="margin: 0;"><strong>⚠️ Importante:</strong> Este enlace expira en 1 hora por seguridad.</p>
                            </div>

                            <p>Si no solicitaste este cambio de contraseña, puedes ignorar este correo de forma segura.</p>
                        </div>

                        <div class="footer">
                            <p>Por seguridad, este enlace solo puede usarse una vez.</p>
                            <p>© 2024 Icesi Trade - Plataforma de comercio universitario</p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(username, resetUrl, resetUrl);
    }

    private String createWelcomeEmailTemplate(String username) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>¡Bienvenido a Icesi Trade!</title>
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                        .container { max-width: 600px; margin: 0 auto; background-color: white; }
                        .header { background: linear-gradient(135deg, #4caf50 0%, #8bc34a 100%); color: white; padding: 30px; text-align: center; }
                        .content { padding: 30px; }
                        .footer { background-color: #f8f9fa; padding: 20px; text-align: center; color: #666; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🎉 ¡Cuenta verificada exitosamente!</h1>
                            <p>¡Ya puedes usar Icesi Trade!</p>
                        </div>

                        <div class="content">
                            <h2>¡Hola %s!</h2>
                            <p>¡Felicidades! Tu cuenta ha sido verificada exitosamente. Ya puedes disfrutar de todas las funcionalidades de Icesi Trade:</p>

                            <div style="background-color: #e8f5e8; border: 1px solid #4caf50; border-radius: 5px; padding: 20px; margin: 20px 0;">
                                <h3 style="color: #2e7d32; margin-top: 0;">✨ Funcionalidades disponibles:</h3>
                                <ul style="color: #2e7d32;">
                                    <li>📦 Publicar productos para vender</li>
                                    <li>🛒 Comprar productos de otros estudiantes</li>
                                    <li>💬 Chatear con otros usuarios</li>
                                    <li>❤️ Gestionar tus productos favoritos</li>
                                    <li>📊 Ver tu historial de compras y ventas</li>
                                </ul>
                            </div>

                            <p><strong>¿Listo para empezar?</strong></p>
                            <p>Visita nuestra plataforma y comienza a explorar todo lo que Icesi Trade tiene para ofrecerte.</p>

                            <div style="text-align: center; margin: 30px 0;">
                                <a href="%s" style="background: linear-gradient(135deg, #4caf50 0%, #8bc34a 100%); color: white; padding: 15px 30px; text-decoration: none; border-radius: 25px; display: inline-block; font-weight: bold;">
                                    🚀 Ir a Icesi Trade
                                </a>
                            </div>
                        </div>

                        <div class="footer">
                            <p>¡Gracias por unirte a nuestra comunidad universitaria!</p>
                            <p>© 2024 Icesi Trade - Plataforma de comercio universitario</p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(username, frontendUrl);
    }
}