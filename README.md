# Demo Project - Calculator and Lucky Draw System

A Spring Boot application featuring a Calculator with undo/redo functionality and an E-commerce Lucky Draw system with comprehensive admin management capabilities.

## Features

### Calculator
- Basic arithmetic operations (addition, subtraction, multiplication, division)
- Undo/redo functionality
- Session-based state management
- Precision up to 6 decimal places
- Negative number support
- Comprehensive error handling

### Lucky Draw System
- Multiple prize types with configurable quantities and probabilities
- Dynamic prize configuration
- Single and multiple consecutive draws
- User draw limit management
- Risk control mechanisms
- High availability and horizontal scalability
- Transactional consistency

### Admin System
- **User Management**: Create, update, delete users with draw limits
- **Activity Management**: Create, update, delete lucky draw activities
- **Prize Management**: Add, update, delete prizes with quantities and probabilities
- **Probability Validation**: Ensure total probability equals 100%
- **Statistics**: View detailed activity statistics and user analytics

## Technology Stack

- **Java 18**
- **Spring Boot 3.4.0**
- **Spring Security** with JWT authentication
- **Spring Data JPA** with H2 database
- **SpringDoc OpenAPI** (Swagger UI)
- **Maven**

## Design Patterns

- **Command Pattern**: Calculator undo/redo operations
- **Strategy Pattern**: Calculator arithmetic operations
- **Repository Pattern**: Data access layer
- **Service Layer Pattern**: Business logic separation
- **DTO Pattern**: Data transfer objects
- **Entity Pattern**: JPA entities

## API Documentation

### Authentication

#### Login
```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=user&password=password"
```

**Response**: JWT token

### Calculator API

#### Add Operation
```bash
curl -X POST "http://localhost:8080/api/calculator/{sessionId}/add" \
  -H "Content-Type: application/json" \
  -d '{"operand": 10.5}'
```

#### Subtract Operation
```bash
curl -X POST "http://localhost:8080/api/calculator/{sessionId}/subtract" \
  -H "Content-Type: application/json" \
  -d '{"operand": 5.2}'
```

#### Multiply Operation
```bash
curl -X POST "http://localhost:8080/api/calculator/{sessionId}/multiply" \
  -H "Content-Type: application/json" \
  -d '{"operand": 3.0}'
```

#### Divide Operation
```bash
curl -X POST "http://localhost:8080/api/calculator/{sessionId}/divide" \
  -H "Content-Type: application/json" \
  -d '{"operand": 2.0}'
```

#### Undo/Redo
```bash
curl -X POST "http://localhost:8080/api/calculator/{sessionId}/undo"
curl -X POST "http://localhost:8080/api/calculator/{sessionId}/redo"
```

#### Get State
```bash
curl -X GET "http://localhost:8080/api/calculator/{sessionId}/state"
```

### Lucky Draw API

#### Single Draw (Requires USER role)
```bash
curl -X POST "http://localhost:8080/api/luckydraw/draw" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "activityId": "summer-2024",
    "userId": "user123"
  }'
```

#### Multiple Draws (Requires USER role)
```bash
curl -X POST "http://localhost:8080/api/luckydraw/draw/multiple" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "activityId": "summer-2024",
    "userId": "user123",
    "drawCount": 3
  }'
```

#### Get Activity Info
```bash
curl -X GET "http://localhost:8080/api/luckydraw/activity/{activityId}"
```

#### Get User Draw History
```bash
curl -X GET "http://localhost:8080/api/luckydraw/history/{userId}?activityId={activityId}"
```

### Admin API (Requires ADMIN role)

#### User Management

**Create User**
```bash
curl -X POST "http://localhost:8080/api/admin/users" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "password123",
    "drawLimit": 10
  }'
```

**Get All Users**
```bash
curl -X GET "http://localhost:8080/api/admin/users" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}"
```

**Update User Draw Limit**
```bash
curl -X PUT "http://localhost:8080/api/admin/users/{userId}" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "drawLimit": 15
  }'
```

**Delete User**
```bash
curl -X DELETE "http://localhost:8080/api/admin/users/{userId}" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}"
```

#### Activity Management

**Create Activity**
```bash
curl -X POST "http://localhost:8080/api/admin/activities" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "activityId": "winter-2024",
    "name": "Winter Lucky Draw",
    "description": "Winter season lucky draw with amazing prizes",
    "maxDrawsPerUser": 5
  }'
```

**Get All Activities**
```bash
curl -X GET "http://localhost:8080/api/admin/activities" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}"
```

**Update Activity**
```bash
curl -X PUT "http://localhost:8080/api/admin/activities/{activityId}" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Winter Lucky Draw",
    "description": "Updated description",
    "maxDrawsPerUser": 8
  }'
```

**Delete Activity**
```bash
curl -X DELETE "http://localhost:8080/api/admin/activities/{activityId}" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}"
```

#### Prize Management

**Add Prize to Activity**
```bash
curl -X POST "http://localhost:8080/api/admin/activities/{activityId}/prizes" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15",
    "description": "Latest iPhone model",
    "quantity": 5,
    "probability": 10.0
  }'
```

**Update Prize**
```bash
curl -X PUT "http://localhost:8080/api/admin/activities/{activityId}/prizes/{prizeId}" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "description": "Updated iPhone model",
    "quantity": 3,
    "probability": 8.0
  }'
```

**Get All Prizes for Activity**
```bash
curl -X GET "http://localhost:8080/api/admin/activities/{activityId}/prizes" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}"
```

**Delete Prize**
```bash
curl -X DELETE "http://localhost:8080/api/admin/activities/{activityId}/prizes/{prizeId}" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}"
```

#### Probability Validation

**Validate Activity Probability**
```bash
curl -X POST "http://localhost:8080/api/admin/activities/{activityId}/validate-probability" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}"
```

**Response Example**:
```json
{
  "isValid": true,
  "totalProbability": 100.0,
  "message": "Probability validation successful. Total: 100.00%",
  "prizeProbabilities": [
    {
      "prizeName": "iPhone 15",
      "probability": 10.0
    },
    {
      "prizeName": "iPad",
      "probability": 15.0
    },
    {
      "prizeName": "AirPods",
      "probability": 25.0
    },
    {
      "prizeName": "Thank You",
      "probability": 50.0
    }
  ]
}
```

#### Statistics

**Get Activity Statistics**
```bash
curl -X GET "http://localhost:8080/api/admin/activities/{activityId}/statistics" \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}"
```

**Response Example**:
```json
{
  "activityId": "summer-2024",
  "activityName": "Summer Lucky Draw",
  "totalDraws": 150,
  "totalUsers": 45,
  "prizeDistribution": {
    "iPhone": 5,
    "iPad": 8,
    "AirPods": 12,
    "Thank You": 125
  },
  "topUsers": [
    {
      "userId": "user123",
      "username": "john_doe",
      "drawCount": 5,
      "wonPrizes": ["iPhone", "Thank You", "Thank You"]
    }
  ],
  "totalProbability": 100.0,
  "probabilityValid": true
}
```

## Authentication & Authorization

### Roles
- **USER**: Can perform draws and view their history
- **ADMIN**: Can manage users, activities, prizes, and view statistics

### JWT Token Usage
- Include `Authorization: Bearer {JWT_TOKEN}` header for protected endpoints
- Tokens include role information for authorization

## Swagger UI

Access the interactive API documentation at:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## Running the Application

### Prerequisites
- Java 18
- Maven

### Build and Run
```bash
# Build the project
./mvnw clean compile

# Run the application
./mvnw spring-boot:run
```

### Test the Application
```bash
# Run tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

## Database

The application uses H2 in-memory database for development:
- **H2 Console**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: `password`

## Sample Data

The application initializes with sample data:
- **Activity**: "summer-2024" with 4 prizes
- **Prizes**: iPhone (10%), iPad (15%), AirPods (25%), Thank You (50%)
- **Total Probability**: 100%

## Error Handling

### HTTP Status Codes
- **200**: Success
- **400**: Bad Request (validation errors)
- **401**: Unauthorized (missing/invalid JWT)
- **403**: Forbidden (insufficient permissions)
- **404**: Not Found
- **409**: Conflict (e.g., user already exists)
- **500**: Internal Server Error

### Error Response Format
```json
{
  "timestamp": "2024-01-01T12:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/luckydraw/draw"
}
```

## Configuration

### Environment Variables
- `SERVER_PORT`: Application port (default: 8080)
- `SPRING_DATASOURCE_URL`: Database URL
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Database schema strategy

### Application Properties
- JWT expiration time
- Database connection settings
- Logging levels
- Swagger UI configuration

## Performance Considerations

### Concurrency
- Pessimistic locking for prize inventory updates
- ConcurrentHashMap for calculator session management
- Transactional consistency for draw operations

### Scalability
- Stateless JWT authentication
- Horizontal scaling support
- Database connection pooling

## Security Features

### JWT Authentication
- Token-based authentication
- Role-based access control
- Token expiration handling

### Input Validation
- Request DTO validation
- Business rule validation
- SQL injection prevention

### Error Handling
- Comprehensive exception handling
- Secure error messages
- Audit logging

## Future Enhancements

### Planned Features
- Real-time notifications
- Advanced analytics dashboard
- Multi-language support
- Mobile app integration
- Payment gateway integration
- Advanced prize categories
- User profile management
- Email notifications
- Push notifications
- Advanced reporting

### Technical Improvements
- Redis caching
- Message queue integration
- Microservices architecture
- Container deployment
- CI/CD pipeline
- Performance monitoring
- Security hardening
- Database optimization

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For support and questions, please contact:
- Email: demo@example.com
- Documentation: http://localhost:8080/swagger-ui.html 