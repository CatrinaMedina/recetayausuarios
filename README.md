# RecetaYa - Microservicio de Usuarios

Microservicio desarrollado en Java 17 y Spring Boot para la Evaluación Parcial 2.

## Funcionalidades

- CRUD de usuarios y roles.
- Autenticación mediante correo y contraseña.
- Generación de tokens JWT.
- Contraseñas protegidas con BCrypt.
- Validaciones de datos y correos duplicados.
- Persistencia mediante Spring Data JPA y H2.

## Requisitos

- Java 17.
- Git.
- Puertos disponibles para ejecutar los servicios.
- Postman para probar los endpoints.

## Clonar el repositorio

git clone https://github.com/CatrinaMedina/recetayausuarios.git

cd recetayausuarios

## Compilar y generar el JAR

Desde Git Bash:

./mvnw clean package

El archivo JAR se genera en la carpeta target.

## Ejecutar localmente

./mvnw spring-boot:run

El servicio funciona en:

http://localhost:8081

## Base de datos

Se utiliza H2 con persistencia en archivo:

jdbc:h2:file:./data/usuariosdb

Hibernate utiliza spring.jpa.hibernate.ddl-auto=update.

## Endpoints principales

- POST /roles
- GET /roles
- GET /roles/{id}
- PUT /roles/{id}
- DELETE /roles/{id}
- POST /usuarios
- GET /usuarios
- GET /usuarios/{id}
- GET /usuarios/correo/{correo}
- PUT /usuarios/{id}
- DELETE /usuarios/{id}
- POST /usuarios/login

## Datos iniciales

En una base de datos nueva, crear primero los roles MEDICO
y FARMACEUTICO mediante POST /roles.

Luego crear los usuarios con un rol existente.

Finalmente, utilizar POST /usuarios/login para obtener
un token JWT.

Para utilizar los endpoints a traves del API Gateway,
tambien deben estar ejecutandose el Gateway y el servicio
de destino.

## Proyecto

RecetaYa - Java: Diseño y Construcción de Soluciones Nativas en Nube.
