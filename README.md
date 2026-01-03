# Task Management Backend API

A multi-user task management backend built with Spring Boot, designed to manage teams, projects, and tasks with clear domain rules and JWT-based authentication.

The project focuses on clean architecture, explicit access control, and maintainable backend design.

---

## ✨ Features

- JWT-based authentication and authorization
- Multi-user and team-based access model
- Project ownership via teams
- Task assignment within project teams
- Role-aware access validation
- Centralized exception handling
- DTO-based API design using MapStruct

---

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3.3**
- Spring Web
- Spring Data JPA (Hibernate)
- Spring Security (JWT)
- PostgreSQL
- MapStruct
- Lombok
- Maven

---

## 🏗 Architecture Overview

The application follows a layered architecture:

- **Controller layer** – REST endpoints and request handling
- **Service layer** – business logic and domain rules
- **Repository layer** – data access with Spring Data JPA
- **Security layer** – JWT authentication and user context
- **DTO & Mapper layer** – API contracts and entity mapping

This structure ensures clear separation of concerns and long-term maintainability.

---

## 🧩 Domain Model

ER Diagram <img width="2480" height="3509" alt="image" src="https://github.com/user-attachments/assets/533ec674-bfa2-40fc-976e-f65bfdc542dd" />


Core domain concepts:

- **User** – authenticated system user
- **Team** – group of users collaborating together
- **Project** – owned by a team and created by a user
- **Task** – belongs to a project and can be assigned to a user
- **UserTeam / UserProject** – explicit join tables for access control

Key domain rules:
- Only team members can access related projects
- Tasks can only be assigned within the project’s team
- Team creators have elevated permissions

*(See ER diagram for full entity relationships.)*

---

## 🔐 Security

- Stateless JWT authentication
- Custom JWT filter (`JwtAuthFilter`)
- Role-based authorization
- Secure password handling
- Centralized access validation enforced at the service layer

Business rules are intentionally validated in the service layer to keep controllers thin and predictable.

---

## 📂 Project Structure

```text
taskmanagement
├── config            # Spring Security configuration
├── controller        # REST controllers
├── dto               # API data transfer objects
├── entity            # JPA entities
├── exception         # Custom domain exceptions
├── filter            # JWT authentication filter
├── mappers           # MapStruct mappers
├── repository        # JPA repositories
├── security          # JWT utilities & user details
└── service
    ├── interfaces    # Service contracts
    └── impl          # Business logic implementations

▶️ Running the Application
Prerequisites

Java 17+

PostgreSQL

Database

Create the database:

CREATE DATABASE taskmanagement;

Configuration

Update application.properties if required:

spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanagement
spring.datasource.username=postgres
spring.datasource.password=your_password
server.port=8086

Run
mvn clean spring-boot:run

📌 Design Decisions & Trade-offs

Explicit join tables (UserTeam, UserProject) are used instead of implicit many-to-many mappings to allow future extensibility (roles, permissions, auditing).

Business rules are enforced in the service layer rather than controllers.

Stateless JWT authentication was chosen for scalability and simplicity.

👤 Author

Burak Erdogan
AI-assisted Full Stack Software Engineer
Java, Spring Boot, React | Cloud-native & scalable systems
