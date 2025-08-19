# ForoHub

Proyecto desarrollado como parte del Challenge Backend de Alura Latam en colaboración con Oracle.

## Descripción

ForoHub es una API REST que simula el funcionamiento de un foro. Los usuarios pueden crear tópicos, listar todos los tópicos, ver detalles de un tópico específico, actualizar y eliminar tópicos. La API implementa autenticación con JWT para proteger los endpoints.

## Estado del proyecto

CRUD básico de tópicos
Base de datos con MySQL y migraciones Flyway
Spring Security configurado
 Autenticación JWT implementada
 Validaciones de negocio
Manejo de errores

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.5.4
- Spring Data JPA
- Spring Security
- MySQL 5.5+
- Flyway Migration
- Maven
- Lombok
- JWT (JSON Web Token) - Auth0
- BCrypt 

## Funcionalidades

### CRUD de Tópicos
- Crear nuevo tópico
- Listar todos los tópicos con paginación
- Mostrar detalle de un tópico específico
- Actualizar tópico existente
- Eliminar tópico

### Autenticación
- Login de usuario
- Generación de token JWT
- Validación de token en cada request

## Instalación y configuración

### Requisitos previos
- Java 21
- MySQL 8.0
- Maven

### Pasos para ejecutar

1. Clonar el repositorio
2. Crear base de datos en MySQL:
   ```sql
   CREATE DATABASE forohub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. Crear usuario para pruebas:
   ```sql
   USE forohub;
   INSERT INTO usuarios (login, clave) 
   VALUES ('admin', '$2a$10$Y50UaMFOxteibQEYLrwuAeRhKZKIqLMOhGh34zABZ1J7WgrEOYWaG');
   ```

4. Configurar credenciales de base de datos en `application.properties`:
   ```properties
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_password
   ```

5. Ejecutar la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```

6. La aplicación estará disponible en: `http://localhost:8080`

## Estructura de base de datos

### Tabla topico
- id (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
- titulo (VARCHAR 255)
- mensaje (TEXT)
- fecha_creacion (TIMESTAMP)
- status (VARCHAR 50)
- autor (VARCHAR 255)
- curso (VARCHAR 255)

### Tabla usuarios
- id (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
- login (VARCHAR 100, UNIQUE)
- clave (VARCHAR 300)

## Endpoints

### Autenticación
- POST `/login` - Login de usuario

### Tópicos (requieren autenticación)
- GET `/topicos` - Listar tópicos
- GET `/topicos/{id}` - Detalle de tópico
- POST `/topicos` - Crear tópico
- PUT `/topicos/{id}` - Actualizar tópico
- DELETE `/topicos/{id}` - Eliminar tópico

## Ejemplos de uso

### 1. Login (obtener token JWT)
```http
POST /login
Content-Type: application/json

{
    "login": "admin",
    "clave": "123456"
}
```

**Respuesta:**
```json
{
    "jwTtoken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9..."
}
```

### 2. Crear tópico
```http
POST /topicos
Authorization: Bearer {token}
Content-Type: application/json

{
    "titulo": "Duda sobre Spring Boot",
    "mensaje": "Como configurar la base de datos",
    "autor": "Juan Perez",
    "curso": "Spring Boot"
}
```

### 3. Listar tópicos
```http
GET /topicos
Authorization: Bearer {token}
```

### 4. Obtener tópico por ID
```http
GET /topicos/1
Authorization: Bearer {token}
```

### 5. Actualizar tópico
```http
PUT /topicos/1
Authorization: Bearer {token}
Content-Type: application/json

{
    "titulo": "Titulo actualizado",
    "mensaje": "Mensaje actualizado"
}
```

### 6. Eliminar tópico
```http
DELETE /topicos/1
Authorization: Bearer {token}
```

## Reglas de negocio

- Todos los campos del tópico son obligatorios
- No se permite crear tópicos duplicados (mismo titulo y mensaje)
- Los endpoints están protegidos con JWT excepto `/login`
- Los tópicos se listan paginados (10 por página)
- Los tópicos se ordenan por fecha de creación ascendente
- Las contraseñas se almacenan encriptadas con BCrypt
- Los tokens JWT expiran en 2 horas

## Estructura del proyecto

```
forohub/
├── src/main/java/com/alura/forohub/
│   ├── controller/
│   │   ├── AutenticacionController.java
│   │   └── TopicoController.java
│   ├── domain/
│   │   ├── topico/
│   │   │   ├── Topico.java
│   │   │   ├── StatusTopico.java
│   │   │   ├── TopicoRepository.java
│   │   │   └── DTOs...
│   │   └── usuario/
│   │       ├── Usuario.java
│   │       ├── UsuarioRepository.java
│   │       ├── AutenticacionService.java
│   │       └── DTOs...
│   ├── infra/
│   │   ├── errores/
│   │   │   └── TratadorDeErrores.java
│   │   └── security/
│   │       ├── SecurityConfigurations.java
│   │       ├── SecurityFilter.java
│   │       ├── TokenService.java
│   │       └── DatosJWTToken.java
│   └── ForohubApplication.java
├── src/main/resources/
│   ├── db/migration/
│   │   ├── V1__create-table-topico.sql
│   │   └── V2__create-table-usuarios.sql
│   └── application.properties
└── pom.xml

