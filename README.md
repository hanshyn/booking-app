# 🏩 Booking Service API

## 📜 Description

This project is an advanced online management system for housing rentals, designed to replace antiquated, manual processes used by a local booking service. The goal is to revolutionize the housing rental experience by providing a seamless and efficient platform for both service administrators and renters. The system addresses challenges related to managing properties, renters, financial transactions, booking records, and limited payment options, aiming to simplify administrative tasks and enhance the renting experience.

## ✨ Features
The Accommodation Booking Service offers the following core functionalities:

* **Role Management:** Defines and manages user roles, including Administrator/Manager and Customer.
* **Accommodation Inventory Management:** Supports full CRUD (Create, Read, Update, Delete) operations for managing properties.
* **Rental Booking Management:** Allows users to create, view, update, and cancel bookings. Includes features for filtering and managing bookings by status and user.
* **User Management:** Handles user registration, authentication, and profile management.
* **Payment Processing:** Facilitates secure payments for bookings, including interaction with a payment provider like Stripe.
* **Notifications:** Provides notifications about significant events (new bookings, cancellations, payments, accommodation status changes) via a service like Telegram.
* **Scheduled Tasks:** Includes functionality for daily checks and processing of expired bookings.

**Non-functional requirements addressed:**

* Support for concurrent users and a significant volume of accommodations and bookings.
---

## 📋 Technology Stack 
The project is built using the following technologies:

### Core

|           |                   Description                   |
|:---------:|:-----------------------------------------------:|
|  Java 17  |    Core programming language of the backend     |
|   Maven   |  Project management and dependency management   |

### Spring

|                         |                                    Description                                    |
|:-----------------------:|:---------------------------------------------------------------------------------:|
|    Spring Boot 3.1.5    |        Framework for building robust, production-ready Spring applications        |
|     Spring Data JPA     |      Simplifies database access through JPA repositories and ORM (Hibernate)      |
|  Spring Boot Security   |        Provides comprehensive authentication and authorization frameworks         |
| Spring Boot Validation  |   Integration of Java Bean Validation (JSR 380) for data constraints and checks   |

### Database & Migrations

|               |                        Description                        |
|:-------------:|:---------------------------------------------------------:|
| MySQL 8.0.33  |           Relational database management system           |
|   Liquibase   |   Database schema change management and version control   |

### Testing

|                 |                                 Description                                 |
|:---------------:|:---------------------------------------------------------------------------:|
|     JUnit 5     |            Popular framework for writing and running unit tests             |
| Testcontainers  |        Library to support integration tests using Docker containers         |
|     Mockito     |         Mocking framework for creating mock objects for unit tests          |

### Additions tools & libraries

|                            |                                   Description                                    |
|:--------------------------:|:--------------------------------------------------------------------------------:|
|           Lombok           | Reduces boilerplate code for Java classes (e.g., getters, setters, constructors) |
|         MapStruct          |                Code generator for type-safe bean-to-bean mappings                |
|            JWT             |   Standard for creating tokens that assert claims, used here for authorization   |
|           Docker           |    Platform for developing, shippping, and running applications in containers    |
|       Docker Compose       |        Tool for defining and running multi-container Docker applications         |
|          Swagger           |    Set of tools built around the OpenAPI Specification for API documentation     |
| Stripe (via `stripe-java`) |          Payment processing platform (used via its Java client library)          |
|        Telegram API        |          Interface for interacting with the Telegram messaging service           |
---

## Endpoints
The application provides a RESTful API with the following main controllers and endpoints. Detailed API documentation can be found in the Swagger UI after running the application.

* **Authentication Controller (`/api/auth`)**: User registration and login (`POST /register`, `POST /login`).
* **User Controller (`/users`)**: User profile management (`GET /me`, `PUT/PATCH /me`) and role updates (`PUT /{id}/role`).
* **Accommodation Controller (`/accommodations`)**: CRUD operations for accommodations (`POST /`, `GET /`, `GET /{id}`, `PUT/PATCH /{id}`, `DELETE /{id}`).
* **Booking Controller (`/bookings`)**: Booking management (`POST /`, `GET /my`, `GET /{id}`, `PUT/PATCH /{id}`, `DELETE /{id}`) and filtering for managers (`GET /?user_id=...&status=...`).
* **Payment Controller (`/payments`)**: Payment initiation (`POST /`) and handling success/cancel redirects (`GET /success`, `GET /cancel`), viewing payments (`GET /?user_id=...`).

*(Note: Some endpoints require authentication (JWT Bearer Token or Basic) and specific roles as indicated in the project description.)*

---
