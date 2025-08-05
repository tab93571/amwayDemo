# Spring Boot Lucky Draw System

A comprehensive Spring Boot application featuring a lucky draw system with admin management, user authentication, and calculator functionality.

## 🚀 Quick Start

```bash
# Clone and run
git clone https://github.com/tab93571/amwayDemo.git
cd amwayDemo
mvn spring-boot:run
```

**Access the application:**
- Main: http://localhost:8080
- Login: http://localhost:8080/login
- Admin: http://localhost:8080/admin
- Lucky Draw: http://localhost:8080/luckydraw
- Calculator: http://localhost:8080/calculator
- API Docs: http://localhost:8080/swagger-ui.html

## 📋 Features

### **Authentication**
- JWT-based authentication
- Role-based access (ADMIN, USER)

### **Lucky Draw System**
- Create and manage draw activities
- Real-time draw execution with inventory management
- User draw history tracking

### **Admin Management**
- Activity and prize management
- Configure prizes with probability distribution
- User administration
- Audit logging

### **Calculator Module**
- Basic arithmetic operations
- Stack-based calculation engine

## 🏗️ Project Structure

```
src/main/java/com/example/demoproject/
├── auth/           # Authentication & Authorization
├── admin/          # Admin Management
├── luckydraw/      # Lucky Draw System
├── calculator/     # Calculator Module
└── security/       # Security Configuration
```

## 🔧 Key Components

### **Authentication (auth/)**
- JWT token generation and validation
- User login/logout functionality

### **Admin Management (admin/)**
- Activity CRUD operations
- Prize management with probability validation
- User administration

### **Lucky Draw (luckydraw/)**
- Draw execution engine
- Prize selection algorithm
- Inventory management

### **Calculator (calculator/)**
- Command pattern implementation
- Stack-based calculation engine

## 🧪 Testing

```bash
# Run all tests
mvn test
```


## 🔐 Security Features

- JWT-based authentication
- Role-based access control
- Password encryption
- Input validation

## 📚 Technologies Used

- **Backend:** Spring Boot 3.4.0, Spring Security, Spring Data JPA
- **Database:** PostgreSQL, H2 (development)
- **Frontend:** Thymeleaf, HTML5, CSS3, JavaScript
- **Authentication:** JWT (JSON Web Tokens)
- **API Documentation:** Swagger/OpenAPI
- **Testing:** JUnit 5, Mockito
- **Build Tool:** Maven

## 🎯 Key Features Summary

- ✅ **Secure Authentication** with JWT tokens
- ✅ **Admin Dashboard** for system management
- ✅ **Lucky Draw Engine** with probability-based prize selection
- ✅ **Calculator Module** with stack-based operations
- ✅ **Real-time Monitoring** and audit logging
- ✅ **Responsive Web Interface** with modern UI
- ✅ **Comprehensive Testing** with high coverage
- ✅ **API Documentation** with Swagger UI
- ✅ **Database Integration** with PostgreSQL/H2
- ✅ **Security Features** with role-based access control 