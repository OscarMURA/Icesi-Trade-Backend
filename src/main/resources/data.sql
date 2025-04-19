--- USERS -> 1234
INSERT INTO USERS ( email, password, name, phone, created_at, updated_at) VALUES
( 'juan.perez@example.com', '$2a$10$/1nBoNr/yazRX57ICxeBdeG8sG7Tnd7Y79QrhXPCU5UdwaGKIJAAO', 'Juan Pérez', '3001234567', CURRENT_TIMESTAMP, NULL),
( 'maria.gomez@example.com', '$2a$10$k5bCEXCgGJQ0CGOeQUFz..N7SSVm5y4KDmVZutpoijlU3SfPXQVXK', 'María Gómez', '3107654321', CURRENT_TIMESTAMP, NULL),
( 'carlos.lopez@example.com', '$2a$10$aATacY/YZu5FBg9BDPtfR.R0oJ75Dxi91o5D7PpBWeJOhiGIOuezy', 'Carlos López', '3159876543', CURRENT_TIMESTAMP, NULL),
( 'laura.martinez@example.com', '$2a$10$x4E491xe0EYmoZqGPXMKjeLeKkQjsrwUf30zrEj/.cNA7t/7EhU9q', 'Laura Martínez', '3205556677', CURRENT_TIMESTAMP, NULL),
( 'andres.rodriguez@example.com', '$2a$10$Eu1oQr5CaMJqZy8U4WO./.NLHkql3AOoZfOX4Ki8Euyu6MXjM3cG6', 'Andrés Rodríguez', '3228889990', CURRENT_TIMESTAMP, NULL);

--- ROLES
INSERT INTO ROLES (name, description) VALUES ('ROLE_ADMIN', 'Administrator');
INSERT INTO ROLES (name, description) VALUES ('ROLE_USER', 'User');

--- PERMISSIONS
--- USERS
INSERT INTO PERMISSIONS (id, name, description) VALUES (1, 'CREATE_USER', 'Create user');
INSERT INTO PERMISSIONS (id, name, description) VALUES (2, 'DELETE_USER', 'Delete user');
INSERT INTO PERMISSIONS (id, name, description) VALUES (3, 'UPDATE_ANY_USER', 'Update any user information');
INSERT INTO PERMISSIONS (id, name, description) VALUES (4, 'UPDATE_OWN_USER', 'Update own user information');
INSERT INTO PERMISSIONS (id, name, description) VALUES (5, 'READ_USER', 'Read user');

--- ROLE_PERMISSIONS
--- ADMIN
INSERT INTO ROLE_PERMISSIONS (id, role, permission) VALUES (1, 1, 1);
INSERT INTO ROLE_PERMISSIONS (id, role, permission) VALUES (2, 1, 2);
INSERT INTO ROLE_PERMISSIONS (id, role, permission) VALUES (3, 1, 3);
INSERT INTO ROLE_PERMISSIONS (id, role, permission) VALUES (4, 1, 4);
INSERT INTO ROLE_PERMISSIONS (id, role, permission) VALUES (5, 1, 5);
--- USER
INSERT INTO ROLE_PERMISSIONS (id, role, permission) VALUES (6, 2, 1);
INSERT INTO ROLE_PERMISSIONS (id, role, permission) VALUES (7, 2, 4);
INSERT INTO ROLE_PERMISSIONS (id, role, permission) VALUES (8, 2, 5);

--- USER_ROLES
INSERT INTO USER_ROLES ( user_id, role_id) VALUES ( 1, 1);
INSERT INTO USER_ROLES ( user_id, role_id) VALUES ( 2, 2);
INSERT INTO USER_ROLES ( user_id, role_id) VALUES ( 3, 2);
INSERT INTO USER_ROLES ( user_id, role_id) VALUES ( 4, 2);
INSERT INTO USER_ROLES ( user_id, role_id) VALUES ( 5, 2);