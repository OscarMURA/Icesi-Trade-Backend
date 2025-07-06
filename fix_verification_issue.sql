-- Script para limpiar datos inconsistentes en la base de datos
-- Ejecutar este script antes de reiniciar la aplicación
-- 1. Eliminar registros huérfanos en user_roles
DELETE FROM user_roles
WHERE user_id NOT IN (
        SELECT id
        FROM users
    );
-- 2. Eliminar registros huérfanos en role_permissions
DELETE FROM role_permissions
WHERE role NOT IN (
        SELECT id
        FROM roles
    )
    OR permission NOT IN (
        SELECT id
        FROM permissions
    );
-- 3. Verificar que no hay más referencias huérfanas
SELECT 'user_roles huérfanos' as tabla,
    COUNT(*) as cantidad
FROM user_roles ur
    LEFT JOIN users u ON ur.user_id = u.id
WHERE u.id IS NULL
UNION ALL
SELECT 'role_permissions huérfanos' as tabla,
    COUNT(*) as cantidad
FROM role_permissions rp
    LEFT JOIN roles r ON rp.role = r.id
    LEFT JOIN permissions p ON rp.permission = p.id
WHERE r.id IS NULL
    OR p.id IS NULL;