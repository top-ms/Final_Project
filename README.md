# Appliance Store — Spring Boot Web App

## Project Overview

**Appliance Store** is a full-featured web application that simulates an online shop for household appliances. The project was developed as part of a test assignment and showcases practical use of Spring Boot, security, localization, and layered architecture.

The application supports multiple user roles — Client, Employee, and Admin — with custom permissions and dynamic UI access, using JWT for authentication and role-based authorization.

## Goals & Requirements

- Implement an online store with multi-role access
- Provide CRUD operations for key entities: `Employee`, `Client`, `Appliance`, `Manufacturer`, `Order`, `OrderRow`
- Add i18n (internationalization), validation, pagination, filtering, and sorting
- Secure the system using Spring Security and JWT tokens (access & refresh)
- Use in-memory H2 database with SQL initialization scripts
- Design the UI using Thymeleaf and Bootstrap

## Roles and Permissions

| Role     | Capabilities |
|----------|--------------|
| **Client** | View and manage own orders |
| **Employee** | Full CRUD on `Client`, `Appliance`, `Manufacturer`, `Employee`; approve orders |
| **Admin** | All employee permissions + create new users of any role |

Access is granted based on role-specific controller mappings (`/admin/**`, `/employee/**`, `/client/**`), secured via JWT.

## Architecture

- **Layered Design**: Controller → Service → Repository
- **DTOs** used to decouple internal models from exposed data
- **ModelMapper** for mapping between DTOs and entities
- **Spring Boot MVC** handles routing, request mapping, and template rendering
- **Validation** via JSR-303 annotations and controller-level checks

## Security

- **Spring Security** + **JWT** (Access + Refresh tokens)
- **Role-based access control**
- **BCrypt** for password encoding
- **Reset Password** functionality

## Localization

Supports **English** and **Ukrainian** via:
- `messages.properties`, `messages_uk.properties`
- Thymeleaf integration with locale switching

## Database

- Uses in-memory **H2** database for demonstration
- Initial data loaded via SQL scripts:
```properties
spring.sql.init.data-locations=classpath:manufacturer.sql,classpath:client.sql,classpath:employee.sql,classpath:appliance.sql
```

Entities:
- `User`, `Client`, `Employee`, `Appliance`, `Manufacturer`, `Order`, `OrderRow`

## Features

-  Authentication & Authorization with JWT
-  Internationalization (i18n)
-  CRUD operations on all core entities
-  Filtering, Pagination, Sorting
-  Password Reset
-  H2 Console (enabled for development)
-  Input validation and exception handling

## Tech Stack

- Java 17
- Spring Boot, Spring Security, Spring Data JPA
- Hibernate, ModelMapper, Lombok
- Thymeleaf + Bootstrap
- H2 Database
- JWT (Access/Refresh)
- Maven, Git

## Running the Project

1. Clone the repository:
```bash
git clone https://github.com/top-ms/Final_Project.git
cd Final_Project
```

2. Run with Maven:
```bash
./mvnw spring-boot:run
```

3. Or run the packaged JAR:
```bash
./mvnw clean package
java -jar target/*.jar
```

4. Access the application at:
```
http://localhost:8080
```

5. Access the H2 Console:
```
http://localhost:8080/h2-console
```

## Example Users

To create in-memory users or pre-load users via SQL. Example roles:
- `admin@example.com / password`
- `employee@example.com / password`
- `client@example.com / password`


## GitHub

Project repository: [github.com/top-ms/Final_Project](https://github.com/top-ms/Final_Project)

---

Developed by **Mykhailo Savchuk**