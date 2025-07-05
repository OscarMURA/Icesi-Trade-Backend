package com.trade.icesi_trade.Service.Interface;

public interface EmailService {

    /**
     * Envía un email de verificación de cuenta
     * 
     * @param to       Email del destinatario
     * @param token    Token de verificación
     * @param username Nombre del usuario
     */
    void sendVerificationEmail(String to, String token, String username);

    /**
     * Envía un email de recuperación de contraseña
     * 
     * @param to       Email del destinatario
     * @param token    Token de recuperación
     * @param username Nombre del usuario
     */
    void sendPasswordResetEmail(String to, String token, String username);

    /**
     * Envía un email de bienvenida
     * 
     * @param to       Email del destinatario
     * @param username Nombre del usuario
     */
    void sendWelcomeEmail(String to, String username);

    /**
     * Verifica si un token es válido
     * 
     * @param token Token a verificar
     * @return true si el token es válido, false en caso contrario
     */
    boolean verifyToken(String token);
}