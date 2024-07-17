# Integrative Course Project: Generic Server in Spring Boot

## Overview
This project involves building a generic server using Spring Boot. The server includes various components such as boundaries, controllers, exceptions, converters, CRUD operations, business logic, manual tests, and automated tests with JUnit. Additionally, the project integrates PostgreSQL with PostGIS, H2 Console, and Hibernate for data persistence and management.

## Project Structure
The project follows a well-structured package hierarchy:

- **boundaries**: Contains the API layer, defining the endpoints for client interactions.
- **controllers**: Manages the HTTP requests and responses, delegating the business logic.
- **myExceptions**: Custom exception handling for better error management.
- **converters**: Responsible for converting between different data representations.
- **crud**: Implements the Create, Read, Update, Delete operations.
- **logics**: Contains the business logic interfaces.
- **logicImplementations**: Implements the business logic interfaces.
- **entities**: Defines the data models and entities used in the application.
- **manualTests**: Scripts and instructions for performing manual tests.
- **auto test with JUnit**: Automated tests written using JUnit.

## Technologies Used

### PostgreSQL
PostgreSQL is an advanced, open-source relational database management system. It is used in this project for data persistence.

### PostGIS
PostGIS is a spatial database extender for PostgreSQL, adding support for geographic objects to the PostgreSQL database.

### H2 Console
H2 Console is a web-based database console for browsing the contents of a database and running SQL queries. It is useful for development and debugging purposes.

### Hibernate
Hibernate is an object-relational mapping (ORM) framework for Java. It provides a framework for mapping an object-oriented domain model to a relational database.

## Getting Started

### Prerequisites
Ensure you have the following installed:
- Java JDK 21
- Gradle
- Docker (for containerization)
- PostgreSQL (for production)
- Git (for version control)

### Setup

1. **Build the project:**
   ```bash
   ./gradlew build
   ```

2. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

### Configuration
The application configuration is managed through the `application.properties` file located in the `src/main/resources` directory. Here are some important configurations:

- **PostgreSQL and PostGIS Configuration:**
  ```properties
  spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
  spring.datasource.username=myuser
  spring.datasource.password=secret
  spring.datasource.driver-class-name=org.postgresql.Driver
  spring.jpa.hibernate.ddl-auto=create-drop
  spring.jpa.show-sql=true
  spring.jpa.properties.hibernate.format_sql=true
  logging.level.org.hibernate.orm.jdbc.bind=trace
  logging.level.org.springframework.transaction=trace
  logging.level.org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping=trace
  spring.jpa.properties.hibernate.dialect=org.hibernate.spatial.dialect.postgis.PostgisDialect
  ```

- **H2 Console Configuration:**
  ```properties
  spring.h2.console.enabled=true
  spring.h2.console.path=/h2-console
  ```

- **Server Port Configuration:**
  ```properties
  server.port=8084
  ```

### Docker
The project includes a Docker setup for containerization.

1. **Build the Docker image:**
   ```bash
   docker build -t app .
   ```

2. **Run the Docker container:**
   ```bash
   docker-compose up
   ```

### Docker Compose Configuration
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8084:8084"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/mydatabase
      SPRING_DATASOURCE_USERNAME: myuser
      SPRING_DATASOURCE_PASSWORD: secret
    depends_on:
      - postgres	
  postgres:
    image: 'postgis/postgis:latest'
    environment:
      - POSTGRES_DB=mydatabase
      - POSTGRES_PASSWORD=secret
      - POSTGRES_USER=myuser
    ports:
      - '5432:5432'
```

### Testing

#### Manual Tests
Manual tests are documented in the `manual_tests` directory. Follow the instructions provided to perform manual tests.

#### Automated Tests
Automated tests are implemented using JUnit. To run the tests:

```bash
./gradlew test
```

## Directory Structure
Here's an overview of the directory structure:

```
.
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── demo/app/
│   │   │       ├── boundaries/
│   │   │       │   ├── MiniAppCommandBoundary.java
│   │   │       │   ├── NewUserBoundary.java
│   │   │       │   ├── ObjectBoundary.java
│   │   │       │   └── UserBoundary.java
│   │   │       ├── controllers/
│   │   │       │   ├── AdminController.java
│   │   │       │   ├── CommandController.java
│   │   │       │   ├── MyNotFoundException.java
│   │   │       │   ├── ObjectController.java
│   │   │       │   └── UserController.java
│   │   │       ├── converters/
│   │   │       │   ├── CommandConverter.java
│   │   │       │   ├── ObjectConverter.java
│   │   │       │   └── UserConverter.java
│   │   │       ├── crud/
│   │   │       │   ├── CommandCrud.java
│   │   │       │   ├── ObjectCrud.java
│   │   │       │   └── UserCrud.java
│   │   │       ├── entities/
│   │   │       │   ├── ApplicationMapToStringConverter.java
│   │   │       │   ├── MiniAppCommandEntity.java
│   │   │       │   ├── ObjectEntity.java
│   │   │       │   └── UserEntity.java
│   │   │       ├── enums/
│   │   │       │   └── Role.java
│   │   │       ├── logics/
│   │   │       │   ├── AdminLogic.java
│   │   │       │   ├── CommandLogic.java
│   │   │       │   ├── EnhancedObjectLogic.java
│   │   │       │   ├── ObjectLogic.java
│   │   │       │   ├── UserLogic.java
│   │   │       │   └── implementations/
│   │   │       │       ├── AdminLogicImpl.java
│   │   │       │       ├── CommandLogicImpl.java
│   │   │       │       ├── EnhancedObjectLogicImpl.java
│   │   │       │       ├── ObjectLogicImpl.java
│   │   │       │       └── UserLogicImpl.java
│   │   │       ├── manualTests/
│   │   │       │   ├── Init.java
│   │   │       │   ├── Initializer.java
│   │   │       │   ├── ObjectInitializer.java
│   │   │       │   └── UserInitializer.java
│   │   │       ├── objects/
│   │   │       │   ├── CommandId.java
│   │   │       │   ├── CreatedBy.java
│   │   │       │   ├── InputValidation.java
│   │   │       │   ├── Location.java
│   │   │       │   ├── ObjectId.java
│   │   │       │   ├── TargetObject.java
│   │   │       │   ├── UserId.java
│   │   │       │   └── Application.java
│   │   │       └── resources/
│   │   │           └── application.properties
│   ├── test/
│   │   └── java/
│   │       └── demo/app/
│   │           ├── AdminTests.java
│   │           ├── CommandTests.java
│   │           ├── ObjectTests.java
│   │           ├── PermissionTests.java
│   │           ├── UserTests.java
│   │           └── Utils.java
├── build.gradle
├── compose.yaml
├── Dockerfile
├── gradlew
├── gradlew.bat
├── README.md
```

## H2 Console
Access the H2 Console at `http://localhost:8084/h2-console` after running the application. Use the following credentials:

- **JDBC URL:** `jdbc:postgresql://127.0.0.1:5432/mydatabase`
- **Username:** `myuser`
- **Password:** `secret`


[FamilyCare360 (2).pptx](https://github.com/user-attachments/files/16263553/FamilyCare360.2.pptx)

## License
Copyright (c) 2024 Yarden Cherry | Noa Gilboa | Netanel Boris Cohen Niazov | Roei Hakmon | Daniel Suliman
