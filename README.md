# 🎰 Spring Boot Lucky Draw System

A comprehensive Spring Boot application featuring a lucky draw system with admin management, user authentication, and calculator functionality.

---

## 🚀 Quick Start

### Prerequisites
- Java 18+
- Maven 3.6+
- PostgreSQL (for production) / H2 (for development)

### Installation & Running
```bash
# Clone the repository
git clone https://github.com/tab93571/amwayDemo.git
cd amwayDemo

# Run the application
mvn spring-boot:run
```

### 🌐 Application URLs
| Page                | URL |
|---------------------|-----|
| **Login**           | http://localhost:8080/login |
| **Admin Dashboard** | http://localhost:8080/admin |
| **Lucky Draw**      | http://localhost:8080/luckydraw |
| **Calculator**      | http://localhost:8080/calculator |
| **Swagger**         | http://localhost:8080/swagger-ui/index.html |


---

## 📋 Core Features

### 🔐 **Authentication & Security**
- JWT-based authentication system
- Role-based access control (ADMIN, USER)
- Password encryption with BCrypt
- Input validation and sanitization

### 🎯 **Lucky Draw System**
- Create and manage draw activities
- Real-time draw execution with inventory management
- Probability-based prize selection algorithm
- User draw history tracking
- Audit logging for all activities

### 👨‍💼 **Admin Management**
- Activity and prize management dashboard
- Configure prizes with probability distribution
- User administration and role management
- Comprehensive audit logging system
- Real-time system monitoring

### 🧮 **Calculator Module**
- Basic arithmetic operations
- Stack-based calculation engine
- Command pattern implementation
- Error handling and validation

---

## 🏗️ Project Architecture

```
src/main/java/com/example/demoproject/
├── 📁 auth/           # Authentication & Authorization
│   ├── AuthController.java
│   └── AuthService.java
├── 📁 admin/          # Admin Management
│   ├── controller/     # Admin Controllers
│   ├── service/        # Admin Services
│   ├── dto/           # Data Transfer Objects
│   └── entity/        # Admin Entities
├── 📁 luckydraw/      # Lucky Draw System
│   ├── controller/     # Draw Controllers
│   ├── service/        # Draw Services
│   ├── util/          # Utility Classes
│   └── entity/        # Draw Entities
├── 📁 calculator/     # Calculator Module
│   ├── controller/     # Calculator Controllers
│   ├── service/        # Calculator Services
│   └── command/       # Command Pattern
└── 📁 security/       # Security Configuration
    ├── SecurityConfig.java
    ├── JwtAuthFilter.java
    └── JwtUtil.java
```

---

## 🔧 Key Components

### **🔐 Authentication Module (`auth/`)**
- JWT token generation and validation
- User login/logout functionality
- Role-based access control
- Session management

### **👨‍💼 Admin Management (`admin/`)**
- Activity CRUD operations
- Prize management with probability validation
- User administration and role management
- Audit logging and monitoring

### **🎰 Lucky Draw (`luckydraw/`)**
- Draw execution engine
- Probability-based prize selection algorithm
- Inventory management system
- User activity tracking

### **🧮 Calculator (`calculator/`)**
- Command pattern implementation
- Stack-based calculation engine
- Arithmetic operation handling
- Error validation and handling

---

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=DrawExecutionServiceTest

# Run with coverage
mvn test jacoco:report
```

### Test Coverage
- ✅ Unit tests for all services
- ✅ Integration tests for controllers
- ✅ Security configuration tests
- ✅ Database integration tests

---

## 🔐 Security Features

| Feature | Description |
|---------|-------------|
| **JWT Authentication** | Secure token-based authentication |
| **Role-Based Access** | ADMIN and USER role management |
| **Password Encryption** | BCrypt password hashing |
| **Input Validation** | Comprehensive request validation |
| **CSRF Protection** | Cross-site request forgery protection |
| **Session Management** | Secure session handling |

---

## 📚 Technology Stack

### **Backend**
- **Framework:** Spring Boot 3.2.8
- **Security:** Spring Security 6.4.1
- **Data:** Spring Data JPA
- **Validation:** Bean Validation

### **Database**
- **Development:** H2 Database
- **Production:** PostgreSQL
- **ORM:** Hibernate 6.6.2

### **Frontend**
- **Template Engine:** Thymeleaf
- **Styling:** HTML5, CSS3
- **Interactivity:** JavaScript

### **Authentication**
- **Token Type:** JWT (JSON Web Tokens)
- **Algorithm:** HS256
- **Library:** JJWT 0.11.5

### **Documentation**
- **API Docs:** Swagger/OpenAPI 2.2.0
- **UI:** Swagger UI

### **Testing**
- **Framework:** JUnit 5
- **Mocking:** Mockito

### **Build & Deploy**
- **Build Tool:** Maven
- **Java Version:** 18

---

## 🎯 Feature Summary

| Feature | Status | Description |
|---------|--------|-------------|
| ✅ **Secure Authentication** | Complete | JWT-based authentication with role management |
| ✅ **Admin Dashboard** | Complete | Full admin interface for system management |
| ✅ **Lucky Draw Engine** | Complete | Probability-based prize selection with inventory |
| ✅ **Calculator Module** | Complete | Stack-based calculator with command pattern |
| ✅ **Real-time Monitoring** | Complete | Audit logging and activity tracking |
| ✅ **Responsive Web Interface** | Complete | Modern UI with Thymeleaf templates |
| ✅ **Comprehensive Testing** | Complete | High test coverage with JUnit and Mockito |
| ✅ **API Documentation** | Complete | Swagger UI for API exploration |
| ✅ **Database Integration** | Complete | PostgreSQL/H2 with JPA/Hibernate |
| ✅ **Security Features** | Complete | Role-based access control and validation |

---

## 🗃️ Demo Data

### **📊 Sample Activities & Prizes**

#### **1. Summer Lucky Draw 2024**
- **Draw Limit:** 10 times per user
- **Prizes:**
  - iPhone 15 Pro: 5 units, 5% probability
  - iPad Pro: 10 units, 10% probability
  - AirPods Pro: 20 units, 15% probability

#### **2. Father's Day Lucky Draw 2024**
- **Draw Limit:** 50 times per user
- **Prizes:**
  - iPhone 15 Pro: 5 units, 5% probability
  - iPad Pro: 10 units, 10% probability
  - AirPods Pro: 20 units, 50% probability

### **👤 Default User Accounts**

| Username | Password | Role | Description |
|----------|----------|------|-------------|
| `admin` | `admin123` | `ROLE_ADMIN` | Full system access |
| `user` | `user123` | `ROLE_USER` | Standard user access |
| `test` | `test123` | `ROLE_USER` | Testing account |

> **Note:** These accounts are automatically created for development and demo purposes.

---

## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/tab93571/amwayDemo.git
   cd amwayDemo
   ```

2. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the application**
   - Open http://localhost:8080/login
   - Login with demo accounts above
   - Explore different modules

4. **Test the features**
   - Try the lucky draw system
   - Open http://localhost:8080/calculator Use the calculator
   - Explore admin features (admin account)

---

