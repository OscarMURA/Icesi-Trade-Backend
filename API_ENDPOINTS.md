# API Endpoints - IcesiTrade Backend

## Base URL

```
http://localhost:8080/api
```

---

## 🔐 Autenticación (AuthApiController)

**Base Path:** `/api/auth`

| Método | Endpoint              | Descripción                             | Parámetros    |
| ------ | --------------------- | --------------------------------------- | ------------- |
| `POST` | `/api/auth/login`     | Iniciar sesión                          | `LogInDto`    |
| `POST` | `/api/auth/register`  | Registrar nuevo usuario                 | `RegisterDto` |
| `GET`  | `/api/auth/role-info` | Obtener información del rol del usuario | -             |

---

## 👥 Usuarios (UserApiController)

**Base Path:** `/api/users`

| Método   | Endpoint                   | Descripción                  | Parámetros              |
| -------- | -------------------------- | ---------------------------- | ----------------------- |
| `GET`    | `/api/users`               | Obtener todos los usuarios   | `roleName` (opcional)   |
| `GET`    | `/api/users/{id}`          | Obtener usuario por ID       | `id`                    |
| `POST`   | `/api/users`               | Crear nuevo usuario          | `RegisterDto`           |
| `PUT`    | `/api/users/{id}`          | Actualizar usuario           | `id`, `UserResponseDto` |
| `DELETE` | `/api/users/{id}`          | Eliminar usuario (ADMIN)     | `id`                    |
| `DELETE` | `/api/users/email/{email}` | Eliminar usuario por email   | `email`                 |
| `GET`    | `/api/users/{id}/roles`    | Obtener roles del usuario    | `id`                    |
| `POST`   | `/api/users/{id}/roles`    | Actualizar roles del usuario | `id`, `List<Long>`      |

---

## 🛍️ Productos (ProductApiController)

**Base Path:** `/api/products`

| Método   | Endpoint                          | Descripción                   | Parámetros                                                                       |
| -------- | --------------------------------- | ----------------------------- | -------------------------------------------------------------------------------- |
| `GET`    | `/api/products`                   | Obtener todos los productos   | `sellerId`, `categoryId`, `minPrice`, `maxPrice`, `status`, `location`, `search` |
| `GET`    | `/api/products/{id}`              | Obtener producto por ID       | `id`                                                                             |
| `GET`    | `/api/products/available`         | Obtener productos disponibles | -                                                                                |
| `POST`   | `/api/products`                   | Crear nuevo producto          | `ProductDto`                                                                     |
| `PUT`    | `/api/products/{id}`              | Actualizar producto           | `id`, `ProductDto`                                                               |
| `DELETE` | `/api/products/{id}`              | Eliminar producto             | `id`                                                                             |
| `POST`   | `/api/products/upload-image`      | Subir imagen de producto      | `file`                                                                           |
| `POST`   | `/api/products/upload-images`     | Subir múltiples imágenes      | `files[]`                                                                        |
| `PATCH`  | `/api/products/{id}/sold`         | Marcar producto como vendido  | `id`                                                                             |
| `POST`   | `/api/products/test-delete-image` | Eliminar imagen de prueba     | `imageUrl`                                                                       |

---

## 📂 Categorías (CategoryApiController)

**Base Path:** `/api/categories`

| Método   | Endpoint               | Descripción                  | Parámetros          |
| -------- | ---------------------- | ---------------------------- | ------------------- |
| `GET`    | `/api/categories`      | Obtener todas las categorías | -                   |
| `GET`    | `/api/categories/{id}` | Obtener categoría por ID     | `id`                |
| `POST`   | `/api/categories`      | Crear nueva categoría        | `CategoryDto`       |
| `PUT`    | `/api/categories/{id}` | Actualizar categoría         | `id`, `CategoryDto` |
| `DELETE` | `/api/categories/{id}` | Eliminar categoría           | `id`                |

---

## 💰 Ventas (SaleApiController)

**Base Path:** `/api/sales`

| Método   | Endpoint                                | Descripción                             | Parámetros             |
| -------- | --------------------------------------- | --------------------------------------- | ---------------------- |
| `GET`    | `/api/sales`                            | Obtener todas las ventas                | `productId`, `buyerId` |
| `GET`    | `/api/sales/{id}`                       | Obtener venta por ID                    | `id`                   |
| `POST`   | `/api/sales`                            | Crear nueva venta                       | `SaleDto`              |
| `PUT`    | `/api/sales/{id}`                       | Actualizar venta                        | `id`, `SaleDto`        |
| `DELETE` | `/api/sales/{id}`                       | Eliminar venta                          | `id`                   |
| `GET`    | `/api/sales/buyer/{buyerId}`            | Obtener ventas por comprador            | `buyerId`              |
| `GET`    | `/api/sales/product/{productId}/offers` | Obtener ofertas pendientes por producto | `productId`            |
| `PUT`    | `/api/sales/{id}/accept`                | Aceptar oferta                          | `id`                   |
| `PUT`    | `/api/sales/{id}/reject`                | Rechazar oferta                         | `id`                   |

---

## ⭐ Reseñas (ReviewApiController)

**Base Path:** `/api/reviews`

| Método   | Endpoint            | Descripción               | Parámetros                |
| -------- | ------------------- | ------------------------- | ------------------------- |
| `GET`    | `/api/reviews`      | Obtener todas las reseñas | `reviewerId`, `productId` |
| `GET`    | `/api/reviews/{id}` | Obtener reseña por ID     | `id`                      |
| `POST`   | `/api/reviews`      | Crear nueva reseña        | `ReviewDto`               |
| `PUT`    | `/api/reviews/{id}` | Actualizar reseña         | `id`, `ReviewDto`         |
| `DELETE` | `/api/reviews/{id}` | Eliminar reseña           | `id`                      |

---

## ❤️ Productos Favoritos (FavoriteProductApiController)

**Base Path:** `/api/favorites`

| Método   | Endpoint                              | Descripción                   | Parámetros            |
| -------- | ------------------------------------- | ----------------------------- | --------------------- |
| `GET`    | `/api/favorites`                      | Obtener todos los favoritos   | `userId`              |
| `POST`   | `/api/favorites`                      | Agregar/quitar de favoritos   | `FavoriteProductDto`  |
| `DELETE` | `/api/favorites/{userId}/{productId}` | Eliminar de favoritos         | `userId`, `productId` |
| `GET`    | `/api/favorites/{userId}/{productId}` | Obtener favorito específico   | `userId`, `productId` |
| `GET`    | `/api/favorites/user/{userId}`        | Obtener favoritos por usuario | `userId`              |

---

## 🔔 Notificaciones (NotificationApiController)

**Base Path:** `/api/notifications`

| Método   | Endpoint                                    | Descripción                        | Parámetros              |
| -------- | ------------------------------------------- | ---------------------------------- | ----------------------- |
| `GET`    | `/api/notifications`                        | Obtener todas las notificaciones   | `userId`                |
| `GET`    | `/api/notifications/{id}`                   | Obtener notificación por ID        | `id`                    |
| `POST`   | `/api/notifications`                        | Crear nueva notificación           | `NotificationDto`       |
| `PUT`    | `/api/notifications/{id}`                   | Actualizar notificación            | `id`, `NotificationDto` |
| `DELETE` | `/api/notifications/{id}`                   | Eliminar notificación              | `id`                    |
| `GET`    | `/api/notifications/user/{userId}`          | Obtener notificaciones por usuario | `userId`                |
| `GET`    | `/api/notifications/user/{userId}/pending`  | Obtener notificaciones pendientes  | `userId`                |
| `PUT`    | `/api/notifications/{id}/read`              | Marcar como leída                  | `id`                    |
| `PUT`    | `/api/notifications/user/{userId}/read-all` | Marcar todas como leídas           | `userId`                |

---

## 💬 Chat (ChatController)

**Base Path:** `/api/chat`

| Método | Endpoint             | Descripción                          | Parámetros |
| ------ | -------------------- | ------------------------------------ | ---------- |
| `GET`  | `/api/chat/users`    | Obtener todos los usuarios para chat | -          |
| `GET`  | `/api/chat/contacts` | Obtener contactos de chat            | `userId`   |

---

## 💬 Mensajes de Chat (ChatMessageController)

**Base Path:** `/api/chat/messages`

| Método   | Endpoint                  | Descripción                  | Parámetros       |
| -------- | ------------------------- | ---------------------------- | ---------------- |
| `GET`    | `/api/chat/messages`      | Obtener mensajes por usuario | `userId`         |
| `POST`   | `/api/chat/messages`      | Crear nuevo mensaje          | `ChatMessageDto` |
| `DELETE` | `/api/chat/messages/{id}` | Eliminar mensaje             | `id`             |

**WebSocket Endpoints:**

- `@MessageMapping("/chat.private")` - Manejo de mensajes privados

---

## 📨 Mensajes (MessageApiController)

**Base Path:** `/api/messages`

| Método   | Endpoint             | Descripción                | Parámetros         |
| -------- | -------------------- | -------------------------- | ------------------ |
| `GET`    | `/api/messages`      | Obtener todos los mensajes | -                  |
| `GET`    | `/api/messages/{id}` | Obtener mensaje por ID     | `id`               |
| `POST`   | `/api/messages`      | Crear nuevo mensaje        | `MessageDto`       |
| `PUT`    | `/api/messages/{id}` | Actualizar mensaje         | `id`, `MessageDto` |
| `DELETE` | `/api/messages/{id}` | Eliminar mensaje           | `id`               |

---

## 🎭 Roles (RoleApiController)

**Base Path:** `/api/roles`

| Método   | Endpoint          | Descripción             | Parámetros      |
| -------- | ----------------- | ----------------------- | --------------- |
| `GET`    | `/api/roles`      | Obtener todos los roles | -               |
| `POST`   | `/api/roles`      | Crear nuevo rol         | `RoleDto`       |
| `PUT`    | `/api/roles/{id}` | Actualizar rol          | `id`, `RoleDto` |
| `DELETE` | `/api/roles/{id}` | Eliminar rol            | `id`            |
| `GET`    | `/api/roles/{id}` | Obtener rol por ID      | `id`            |

---

## 📋 Resumen de Endpoints por Módulo

### Autenticación y Usuarios

- **Auth:** 3 endpoints
- **Users:** 8 endpoints
- **Roles:** 5 endpoints

### Productos y Categorías

- **Products:** 10 endpoints
- **Categories:** 5 endpoints

### Comercio

- **Sales:** 9 endpoints
- **Reviews:** 5 endpoints
- **Favorites:** 5 endpoints

### Comunicación

- **Chat:** 2 endpoints
- **Chat Messages:** 3 endpoints + WebSocket
- **Messages:** 5 endpoints

### Sistema

- **Notifications:** 9 endpoints

**Total:** 69 endpoints + 1 WebSocket endpoint

---

## 🔧 Configuración CORS

Todos los controladores están configurados con `@CrossOrigin` para permitir peticiones desde:

- `http://localhost:5173` (Vite dev server)
- `http://localhost:3000` (React dev server)
- URLs configuradas en `application.properties`

---

## 📝 Notas Importantes

1. **Autenticación:** La mayoría de endpoints requieren autenticación JWT
2. **Roles:** Algunos endpoints requieren roles específicos (ADMIN, USER)
3. **Validación:** Los DTOs incluyen validaciones con `@Valid`
4. **Documentación:** Todos los endpoints están documentados con Swagger/OpenAPI
5. **WebSocket:** El chat en tiempo real usa WebSocket en `/chat.private`
