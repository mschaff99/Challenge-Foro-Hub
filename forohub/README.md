# ForoHub

Proyecto desarrollado como parte del Challenge Backend de Alura Latam en colaboración con Oracle.

## Descripción

ForoHub es una API REST que simula el funcionamiento de un foro. Los usuarios pueden crear tópicos, listar todos los tópicos, ver detalles de un tópico específico, actualizar y eliminar tópicos. La API implementa autenticación con JWT para proteger los endpoints.

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.5.4
- Spring Data JPA
- Spring Security
- MySQL
- Flyway
- Maven
- Lombok
- JWT (JSON Web Token)

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
   CREATE DATABASE forohub;
   ```

3. Configurar credenciales de base de datos en `application.properties`

4. Ejecutar la aplicación:
   ```
   ./mvnw spring-boot:run
   ```

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

### Login
```http
POST /login
Content-Type: application/json

{
    "login": "admin",
    "clave": "123456"
}
```

### Crear tópico
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

### Listar tópicos
```http
GET /topicos
Authorization: Bearer {token}
```

## Reglas de negocio

- Todos los campos del tópico son obligatorios
- No se permite crear tópicos duplicados (mismo titulo y mensaje)
- Los endpoints están protegidos con JWT excepto `/login`
- Los tópicos se listan paginados (10 por página)
- Los tópicos se ordenan por fecha de creación ascendente

