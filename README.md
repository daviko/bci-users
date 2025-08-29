# User Management Microservice

A Spring Boot microservice for **user registration** and **authentication** with JWT token-based security. This
application provides RESTful APIs for **sign-up** and **login** operations with comprehensive validation and
security features.

---

## 🚀 Features

- **User Registration** – Create new users with validation
- **JWT Authentication** – Secure token-based authentication
- **Password Encryption** – BCrypt password hashing
- **Data Validation** – Comprehensive input validation with custom regex patterns
- **H2 Database** – In-memory database with console access
- **Error Handling** – Structured error responses with proper HTTP status codes
- **Unit Testing** – Comprehensive test coverage with JUnit and Mockito

---

## 📋 API Endpoints

### Sign Up

**POST** `/api/v1/sign-up`

**Description:** Creates a new user account

**Request Body:**

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "a1Bcdefg23",
  "phones": [
    {
      "number": 1234567890,
      "citycode": 1,
      "contrycode": "57"
    }
  ]
}
```

## Login

**GET /api/v1/login**

Description: Authenticates user and returns profile data

Headers:

Authorization: Bearer <jwt_token>

Response: Returns user details with refreshed JWT token

---

### 🛠️ Technical Stack

Java 11 – Programming language

Spring Boot 2.5.14 – Application framework

Gradle 7.4 – Build tool

H2 Database – In-memory database

JWT – JSON Web Tokens for authentication

JUnit 5 – Unit testing framework

Mockito – Mocking framework for tests

---

### 🚀 Building and Execution

Prerequisites

Java 11 or later

Gradle 7.4

Building the Project

---

### Clone the repository (if applicable)

```
git clone https://github.com/daviko/bci-users.git
cd bci-users
```

### Build the project

```
./gradlew build
```

### Build without tests

```
./gradlew build -x test
```

Running the Application

### Run the application

```
./gradlew bootRun
```

### Run with specific profile

```
./gradlew bootRun --args='--spring.profiles.active=dev'
```

Running Tests

### Run all tests

```
./gradlew test
```

### Run tests with coverage report

```
./gradlew test jacocoTestReport
```

### Run specific test class

```
./gradlew test --tests "UserControllerTest"
```

### Generate test coverage report

```
./gradlew jacocoTestReport
```

---

## 📊 Database Access

The application uses an H2 in-memory database.

H2 Console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb

Username: sa

Password: (leave empty)

---

## 🔐 Security Features

Password Requirements

Minimum 8 characters, maximum 12 characters

Exactly one uppercase letter

Exactly two numbers

Lowercase letters and numbers only

Email Validation

Standard email format validation using regex

Must follow pattern: aaaaaaa@undominio.algo

JWT Token Security

Algorithm: HS512 with 512-bit secure key

Token expiration: 1 hour

Automatic token refresh on login

---

## 🧪 Testing

Test Coverage

The project maintains over 80% test coverage including:

Controller and Service layer tests with Mockito

Security component tests

Exception handling tests

Running Specific Tests

### Run controller tests

```
./gradlew test --tests "*ControllerTest"
```

### Run service tests

```
./gradlew test --tests "*ServiceTest"
```

### Run security tests

```
./gradlew test --tests "*SecurityTest"
```

---

## 📋 API Examples

### Successful Sign-Up

**Request:**

```
curl -X POST http://localhost:8080/api/v1/sign-up \
-H "Content-Type: application/json" \
-d '{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "a1Bcdefg23",
  "phones": [
    {
      "number": 1234567890,
      "citycode": 1,
      "contrycode": "57"
    }
  ]
}'
```

**Response (201 Created):**

```json
{
  "id": "e5c6cf84-8860-4c00-91cd-22d3be28904e",
  "created": "2023-10-15T10:30:00.123",
  "lastLogin": "2023-10-15T10:30:00.123",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "isActive": true,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "$2a$10$abcdefghijklmnopqrstuvwxyz123456",
  "phones": [
    {
      "number": 1234567890,
      "citycode": 1,
      "contrycode": "57"
    }
  ]
}
```

### Successful Login

**Request:**

```
curl -X GET http://localhost:8080/api/v1/login \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

**Response (200 OK):**

```json
{
  "id": "e5c6cf84-8860-4c00-91cd-22d3be28904e",
  "created": "2023-10-15T10:30:00.123",
  "lastLogin": "2023-10-15T11:30:00.123",
  "token": "eyJhbGciOiJIUzI1NiJ9...new_token...",
  "isActive": true,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "$2a$10$abcdefghijklmnopqrstuvwxyz123456",
  "phones": [
    {
      "number": 1234567890,
      "citycode": 1,
      "contrycode": "57"
    }
  ]
}
```

---

## 🗺️ UML Diagrams

The diagrams/ folder includes:

### Component Diagram – Shows the architecture and component relationships

![component_diagram.png](diagrams/component_diagram.png)

### Sequence Diagram (Sign-Up) – Illustrates the user registration flow

![sequence_signup.png](diagrams/sequence_signup.png)

### Sequence Diagram (Login) – Shows the user authentication process

![sequence_login.png](diagrams/sequence_login.png)

### Sequence Diagram (Security Filter) – Demonstrates JWT filter operation

![sequence_security_filter.png](diagrams/sequence_security_filter.png)

You can check each diagram by pasting the content of puml files into this [online viewer](https://www.planttext.com/)

---

## 🐛 Troubleshooting

### Common Issues

Port already in use: Change server.port in application.properties

JWT Secret too short: Ensure the secret is at least 64 characters

H2 console not accessible: Verify spring.h2.console.enabled=true

Validation errors: Check password and email format requirements

### Logs

Enable debug logging for troubleshooting:

```
logging.level.com.bci.user=DEBUG
logging.level.org.springframework.security=DEBUG
```
