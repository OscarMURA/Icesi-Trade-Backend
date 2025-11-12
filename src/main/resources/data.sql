-- USERS
INSERT INTO users (email, password, name, phone, created_at, updated_at, enabled) VALUES
('juan.perez@example.com', '$2a$10$/1nBoNr/yazRX57ICxeBdeG8sG7Tnd7Y79QrhXPCU5UdwaGKIJAAO', 'Juan Pérez', '3001234567', CURRENT_TIMESTAMP, NULL, true)
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (email, password, name, phone, created_at, updated_at, enabled) VALUES
('maria.gomez@example.com', '$2a$10$k5bCEXCgGJQ0CGOeQUFz..N7SSVm5y4KDmVZutpoijlU3SfPXQVXK', 'María Gómez', '3107654321', CURRENT_TIMESTAMP, NULL, true)
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (email, password, name, phone, created_at, updated_at, enabled) VALUES
('carlos.lopez@example.com', '$2a$10$aATacY/YZu5FBg9BDPtfR.R0oJ75Dxi91o5D7PpBWeJOhiGIOuezy', 'Carlos López', '3159876543', CURRENT_TIMESTAMP, NULL, true)
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (email, password, name, phone, created_at, updated_at, enabled) VALUES
('laura.martinez@example.com', '$2a$10$x4E491xe0EYmoZqGPXMKjeLeKkQjsrwUf30zrEj/.cNA7t/7EhU9q', 'Laura Martínez', '3205556677', CURRENT_TIMESTAMP, NULL, true)
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (email, password, name, phone, created_at, updated_at, enabled) VALUES
('andres.rodriguez@example.com', '$2a$10$Eu1oQr5CaMJqZy8U4WO./.NLHkql3AOoZfOX4Ki8Euyu6MXjM3cG6', 'Andrés Rodríguez', '3228889990', CURRENT_TIMESTAMP, NULL, true)
ON CONFLICT (email) DO NOTHING;


-- ROLES
INSERT INTO roles (name, description) VALUES ('ROLE_ADMIN', 'Administrator')
ON CONFLICT (name) DO NOTHING;

INSERT INTO roles (name, description) VALUES ('ROLE_USER', 'User')
ON CONFLICT (name) DO NOTHING;

-- PERMISSIONS
INSERT INTO permissions (id, name, description) VALUES (1, 'CREATE_USER', 'Create user')
ON CONFLICT (id) DO NOTHING;

INSERT INTO permissions (id, name, description) VALUES (2, 'DELETE_USER', 'Delete user')
ON CONFLICT (id) DO NOTHING;

INSERT INTO permissions (id, name, description) VALUES (3, 'UPDATE_ANY_USER', 'Update any user information')
ON CONFLICT (id) DO NOTHING;

INSERT INTO permissions (id, name, description) VALUES (4, 'UPDATE_OWN_USER', 'Update own user information')
ON CONFLICT (id) DO NOTHING;

INSERT INTO permissions (id, name, description) VALUES (5, 'READ_USER', 'Read user')
ON CONFLICT (id) DO NOTHING;

-- ROLE_PERMISSIONS
INSERT INTO role_permissions (role, permission) VALUES (1, 1)
ON CONFLICT (role, permission) DO NOTHING;

INSERT INTO role_permissions (role, permission) VALUES (1, 2)
ON CONFLICT (role, permission) DO NOTHING;

INSERT INTO role_permissions (role, permission) VALUES (1, 3)
ON CONFLICT (role, permission) DO NOTHING;

INSERT INTO role_permissions (role, permission) VALUES (1, 4)
ON CONFLICT (role, permission) DO NOTHING;

INSERT INTO role_permissions (role, permission) VALUES (1, 5)
ON CONFLICT (role, permission) DO NOTHING;

INSERT INTO role_permissions (role, permission) VALUES (2, 1)
ON CONFLICT (role, permission) DO NOTHING;

INSERT INTO role_permissions (role, permission) VALUES (2, 4)
ON CONFLICT (role, permission) DO NOTHING;

INSERT INTO role_permissions (role, permission) VALUES (2, 5)
ON CONFLICT (role, permission) DO NOTHING;

-- USER_ROLES
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1)
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id) VALUES (2, 2)
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id) VALUES (3, 2)
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id) VALUES (4, 2)
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id) VALUES (5, 2)
ON CONFLICT (user_id, role_id) DO NOTHING;

-- Tipos de notificación
INSERT INTO type_notification (id, name) VALUES (1, 'MESSAGE') ON CONFLICT (id) DO NOTHING;
INSERT INTO type_notification (id, name) VALUES (2, 'SYSTEM') ON CONFLICT (id) DO NOTHING;
INSERT INTO type_notification (id, name) VALUES (3, 'ORDER') ON CONFLICT (id) DO NOTHING;
INSERT INTO type_notification (id, name) VALUES (4, 'PRODUCT') ON CONFLICT (id) DO NOTHING;