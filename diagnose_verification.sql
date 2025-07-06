-- Script de diagnóstico para verificar el estado de verificación de emails
-- Ejecutar este script para diagnosticar problemas con la verificación
-- 1. Verificar usuarios y su estado de habilitación
SELECT u.id,
    u.email,
    u.enabled,
    u.created_at,
    COUNT(ev.id) as verification_tokens_count
FROM users u
    LEFT JOIN email_verifications ev ON u.email = ev.email
    AND ev.type = 'EMAIL_VERIFICATION'
GROUP BY u.id,
    u.email,
    u.enabled,
    u.created_at
ORDER BY u.created_at DESC;
-- 2. Verificar tokens de verificación y su estado
SELECT ev.id,
    ev.email,
    ev.used,
    ev.expires_at,
    ev.created_at,
    u.enabled as user_enabled,
    CASE
        WHEN ev.used = true
        AND u.enabled = false THEN 'PROBLEMA: Token usado pero usuario no habilitado'
        WHEN ev.used = false
        AND u.enabled = true THEN 'PROBLEMA: Usuario habilitado pero token no usado'
        WHEN ev.used = true
        AND u.enabled = true THEN 'OK: Token usado y usuario habilitado'
        WHEN ev.used = false
        AND u.enabled = false THEN 'OK: Token no usado y usuario no habilitado'
        ELSE 'ESTADO DESCONOCIDO'
    END as status
FROM email_verifications ev
    LEFT JOIN users u ON ev.email = u.email
WHERE ev.type = 'EMAIL_VERIFICATION'
ORDER BY ev.created_at DESC;
-- 3. Buscar tokens específicos (reemplaza 'tu_token_aqui' con el token real)
-- SELECT * FROM email_verifications WHERE token = 'tu_token_aqui';
-- 4. Verificar si hay tokens duplicados
SELECT email,
    COUNT(*) as token_count,
    COUNT(
        CASE
            WHEN used = true THEN 1
        END
    ) as used_count,
    COUNT(
        CASE
            WHEN used = false THEN 1
        END
    ) as unused_count
FROM email_verifications
WHERE type = 'EMAIL_VERIFICATION'
GROUP BY email
HAVING COUNT(*) > 1
ORDER BY token_count DESC;
-- 5. Verificar tokens expirados
SELECT ev.id,
    ev.email,
    ev.used,
    ev.expires_at,
    CASE
        WHEN ev.expires_at < NOW() THEN 'EXPIRADO'
        ELSE 'VÁLIDO'
    END as expiration_status
FROM email_verifications ev
WHERE ev.type = 'EMAIL_VERIFICATION'
    AND ev.expires_at < NOW()
ORDER BY ev.expires_at DESC;
-- 6. Estadísticas generales
SELECT 'Total usuarios' as metric,
    COUNT(*) as count
FROM users
UNION ALL
SELECT 'Usuarios habilitados' as metric,
    COUNT(*) as count
FROM users
WHERE enabled = true
UNION ALL
SELECT 'Usuarios no habilitados' as metric,
    COUNT(*) as count
FROM users
WHERE enabled = false
UNION ALL
SELECT 'Total tokens de verificación' as metric,
    COUNT(*) as count
FROM email_verifications
WHERE type = 'EMAIL_VERIFICATION'
UNION ALL
SELECT 'Tokens usados' as metric,
    COUNT(*) as count
FROM email_verifications
WHERE type = 'EMAIL_VERIFICATION'
    AND used = true
UNION ALL
SELECT 'Tokens no usados' as metric,
    COUNT(*) as count
FROM email_verifications
WHERE type = 'EMAIL_VERIFICATION'
    AND used = false
UNION ALL
SELECT 'Tokens expirados' as metric,
    COUNT(*) as count
FROM email_verifications
WHERE type = 'EMAIL_VERIFICATION'
    AND expires_at < NOW();