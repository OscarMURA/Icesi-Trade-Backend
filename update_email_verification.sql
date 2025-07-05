-- Script para agregar verificación de email
-- Ejecutar este script en tu base de datos PostgreSQL
-- 1. Agregar columna enabled a la tabla USERS si no existe
DO $$ BEGIN IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_name = 'users'
        AND column_name = 'enabled'
) THEN
ALTER TABLE users
ADD COLUMN enabled BOOLEAN NOT NULL DEFAULT false;
END IF;
END $$;
-- 2. Crear tabla EMAIL_VERIFICATIONS
CREATE TABLE IF NOT EXISTS email_verifications (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT false,
    type VARCHAR(50) NOT NULL
);
-- 3. Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_email_verifications_token ON email_verifications(token);
CREATE INDEX IF NOT EXISTS idx_email_verifications_email ON email_verifications(email);
CREATE INDEX IF NOT EXISTS idx_email_verifications_expires ON email_verifications(expires_at);
-- 4. Habilitar usuarios existentes (opcional - solo si quieres que los usuarios existentes estén habilitados)
-- UPDATE users SET enabled = true WHERE enabled IS NULL OR enabled = false;
-- 5. Verificar que las tablas se crearon correctamente
SELECT 'Tabla USERS actualizada' as status,
    COUNT(*) as total_users
FROM users
UNION ALL
SELECT 'Tabla EMAIL_VERIFICATIONS creada' as status,
    COUNT(*) as total_verifications
FROM email_verifications;