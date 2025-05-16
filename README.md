# 🏩 Booking Service API

## 📜 Description

This project is an advanced online management system for housing rentals, designed to replace antiquated, manual processes used by a local booking service. The goal is to revolutionize the housing rental experience by providing a seamless and efficient platform for both service administrators and renters. The system addresses challenges related to managing properties, renters, financial transactions, booking records, and limited payment options, aiming to simplify administrative tasks and enhance the renting experience.

---
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

## 	📍 Endpoints
The application provides a RESTful API with the following main controllers and endpoints. Detailed API documentation can be found in the Swagger UI after running the application.

* **Authentication Controller (`/api/auth`)**: User registration and login (`POST /register`, `POST /login`).
* **User Controller (`/users`)**: User profile management (`GET /me`, `PUT/PATCH /me`) and role updates (`PUT /{id}/role`).
* **Accommodation Controller (`/accommodations`)**: CRUD operations for accommodations (`POST /`, `GET /`, `GET /{id}`, `PUT/PATCH /{id}`, `DELETE /{id}`).
* **Booking Controller (`/bookings`)**: Booking management (`POST /`, `GET /my`, `GET /{id}`, `PUT/PATCH /{id}`, `DELETE /{id}`) and filtering for managers (`GET /?user_id=...&status=...`).
* **Payment Controller (`/payments`)**: Payment initiation (`POST /`) and handling success/cancel redirects (`GET /success`, `GET /cancel`), viewing payments (`GET /?user_id=...`).

*(Note: Some endpoints require authentication (JWT Bearer Token or Basic) and specific roles as indicated in the project description.)*

---
## 🚀 Postman collections
[Postman collections]()

---
## 💻 How to run the project locally
### 1. Download [Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and [Docker Desktop](https://www.docker.com/).
### 2. Clone the project:
````bash
git clone https://github.com/hanshyn/booking-app.git
````
### 3.1 Add a .env file to the root folder of the cloned project and add your configuration.

Example config:
```
POSTGRES_USER=YOUR_USER_NAME_DB
POSTGRES_PASSWORD=YOUR_PASSWORD_DB
POSTGRES_DATABASE=YOUR_NAME_DB
POSTGRES_LOCAL_PORT=5434
POSTGRES_DOCKER_PORT=5432

SPRING_LOCAL_PORT=8088
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005
```
### 3.2 Add the application.properties file
Example application.properties:
````
# Example configuration file for booking-app
# Copy this to 'application.properties' and fill in your real secrets

spring.datasource.url=jdbc:postgresql://localhost:5432/booking_app
spring.datasource.username=your_user_name_db
spring.datasource.password=your_password_db
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.open-in-view=false

server.servlet.context-path=/booking

jwt.expiration=300000
jwt.secret=your_jwt_secret_here

stripe.apikey=sk_test_your_apikey_stripe
stripe.webhook.secret=your_stripe_webhook

bot.token=your_telegram_token
bot.name=your_boot_name

site.url=https://your_url_site

spring.docker.compose.enabled=false

````

### 4. Build the project:
```bash
mvn clean install
```

###  5. Build the Docker images and start the containers:
````bash
docker-compose up --build
````
---

## 🌐 How to run the project in Codespaces
### 1. Create a Codespaces on master:
![The Codespaces](images/codespaces.png "Create a codespaces on master")

### 2. Checking and Setting up Java in Codespaces:
* verify if Java is installed in your Codespace:
```bash
java -version
```
> 💁
> If the installed version is below 17, or if Java is not installed at all, you will need to install it
> ```bash 
> sudo apt update
> sudo apt install openjdk-17-jdk 
> ```
> Then, check your JAVA_HOME environment variable:
>  ```bash 
> echo $JAVA_HOME
> ```
> If the path is incorrect (e.g., pointing to an older version), set the correct one. For example, if using OpenJDK 17:
>  ```bash 
> export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
> export PATH=$JAVA_HOME/bin:$PATH
> ```

### 3. Add a .env file to the root folder and add your configuration.

Example config:
```
MYSQLDB_USER=root
MYSQLDB_USER_PASSWORD=password
MYSQLDB_USER_DATABASE=db_book_store
MYSQLDB_USER_LOCAL_PORT=3308
MYSQLDB_USER_DOCKER_PORT=3306

SPRING_LOCAL_PORT=8088
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005
```
### 4. Build the project:
```bash
mvn clean install
```

### 5. Start the project on Cpdespaces:
```bash
docker-compose up
```
### 6. To access the Swagger UI for the application running in Codespaces, follow these steps:

* Find the application URL: In your Codespaces environment, look for the "PORTS" tab (as shown in the screenshot you provided). Find the forwarded address for the port your application is running on (commonly 8080 or 8081, check your application's configuration if unsure). This address is the base URL for your application.
* Copy the URL: Copy the forwarded address displayed next to the relevant port.
* Open in browser: Open a new tab in your web browser.
* Access Swagger UI: Paste the copied URL into the address bar.
* Append the Swagger path: To the end of the URL, add the path `/swagger-ui/index.html.` The full address should look something like `https://your-codespace-url.github.dev/swagger-ui/index.html`
* Go: Press Enter to navigate to the Swagger UI page. From there, you can explore and test the API endpoints.