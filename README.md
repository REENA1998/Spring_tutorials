# Spring Boot Microservices Tutorial Repository

A comprehensive collection of Spring Boot microservices demonstrating various architectural patterns, security implementations, and inter-service communication.

## 📋 Table of Contents
- [Services Overview](#services-overview)
- [Topics Covered](#topics-covered)
- [Architecture Diagram](#architecture-diagram)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Detailed Explanations](#detailed-explanations)

---

## 🏗️ Services Overview

### 1. **Eureka Server** (Port: 8761)
- **Purpose**: Service Discovery & Registration
- **Key Features**:
  - Centralized service registry
  - Health monitoring of registered services
  - Load balancing support

### 2. **API Gateway** (Port: 8085)
- **Purpose**: Single entry point for all microservices
- **Key Features**:
  - Request routing with Spring Cloud Gateway
  - Load balancing (lb://)
  - Custom filters & predicates
  - Response header manipulation
  - Status code customization

### 3. **Student Service**
- **Purpose**: Manages student data
- **Key Features**:
  - REST API for student operations
  - Feign Client for inter-service communication
  - Service discovery integration

### 4. **School Service**
- **Purpose**: Manages school data and coordinates with Student Service
- **Key Features**:
  - REST API for school operations
  - Feign Client with OAuth2 integration
  - Custom Feign Interceptors
  - JPA repository pattern
  - Data initialization on startup

### 5. **Beer API**
- **Purpose**: Demo API for beverage management
- **Key Features**:
  - Simple REST endpoints
  - Service registration with Eureka

### 6. **Weather Service**
- **Purpose**: Weather data API with comprehensive security
- **Key Features**:
  - JWT Authentication
  - Role-Based Access Control (RBAC)
  - Permission-based authorization
  - Spring Security configuration
  - User management
  - Caching implementation

---

## 📚 Topics Covered

### **1. Microservices Architecture**
- Service decomposition
- Service discovery pattern
- API Gateway pattern
- Inter-service communication

### **2. Service Discovery (Eureka)**
- Service registration
- Service discovery
- Health checks
- Load balancing

### **3. API Gateway (Spring Cloud Gateway)**
- Route configuration
- Predicates (Path-based routing)
- Filters (SetStatus, AddResponseHeader)
- Load balancing with `lb://` protocol
- Request/Response transformation

### **4. Inter-Service Communication**
- **Feign Client**:
  - Declarative REST clients
  - Custom interceptors
  - OAuth2 integration
  - Header propagation

### **5. Spring Security** (Weather Service)
- **Authentication**:
  - JWT token generation
  - JWT token validation
  - Custom JWT filter
  - UserDetailsService implementation
  
- **Authorization**:
  - Role-Based Access Control (RBAC)
  - Permission-based access
  - Method-level security (@EnableMethodSecurity)
  - Endpoint security
  
- **Password Management**:
  - BCrypt password encoding
  - Custom user storage

### **6. Security Concepts Implemented**
```
Weather Service Security:
├── JWT Authentication Filter
├── Custom UserDetailsService
├── BCrypt Password Encoder
├── Role & Permission Entities
├── AuthenticationManager
├── SecurityFilterChain
└── Method Security
```

### **7. Data Management**
- JPA/Hibernate
- Repository pattern
- Data initialization (CommandLineRunner)
- Entity relationships

### **8. Configuration Management**
- application.yml / application.properties
- Service-specific configurations
- Eureka client configuration
- Security configuration

### **9. Design Patterns**
- Repository Pattern
- Service Layer Pattern
- DTO Pattern
- Interceptor Pattern
- Filter Pattern

---

## 🔐 Spring Security Implementation Details

### **Weather Service - Security Architecture**

#### **1. Authentication Flow**
```
Client Request → JwtAuthFilter → Extract JWT Token 
→ Validate Token → Load User Details → Set Authentication 
→ SecurityFilterChain → Controller
```

#### **2. Security Components**

**SecurityConfig.java**
- Configures HTTP security
- Defines endpoint access rules
- Registers custom JWT filter
- Disables CSRF for stateless API

**JwtAuthFilter.java**
- Intercepts all requests
- Validates JWT tokens
- Sets authentication context

**CustomUserDetailsService.java**
- Loads user from database
- Returns UserDetails for authentication

**Entities**:
- `Users`: User credentials and profile
- `Role`: User roles (ADMIN, USER, etc.)
- `Permissions`: Fine-grained permissions (WEATHER_READ, WEATHER_WRITE, WEATHER_DELETE)

**AuthController.java**
- `/authenticate` endpoint for login
- Generates JWT tokens

#### **3. Authorization Levels**
1. **Endpoint Level**: `.requestMatchers("/authenticate").permitAll()`
2. **Role Level**: `.hasRole("ADMIN")`
3. **Permission Level**: `.hasAuthority("WEATHER_READ")`
4. **Method Level**: `@PreAuthorize("hasAuthority('WEATHER_READ')")`

---

## 🚀 API Gateway Route Explanation

### **What Happens When You Hit `/school/**`?**

When a request comes to: `http://localhost:8085/school/students`

#### **Step-by-Step Internal Flow:**

1. **Gateway Receives Request**
   - Request hits API Gateway on port 8085
   - Path: `/school/students`

2. **Route Matching**
   ```yaml
   - id: school_route
     uri: lb://SCHOOL-SERVICE
     predicates:
       - Path=/school/**
   ```
   - Gateway checks all configured routes
   - Matches `school_route` because path starts with `/school/`

3. **Service Discovery**
   - `lb://SCHOOL-SERVICE` tells Gateway to use load balancer
   - Gateway queries Eureka Server (localhost:8761)
   - Eureka returns available instances of SCHOOL-SERVICE
   - Example: `http://localhost:8082` (school-service port)

4. **Request Transformation**
   - Original: `http://localhost:8085/school/students`
   - Forwarded to: `http://localhost:8082/school/students`

5. **Filters Applied** (Before forwarding)
   ```yaml
   filters:
     - name: SetStatus
       args:
         status: 401   # Sets response status to 401
     - AddResponseHeader=X-Service, SchoolService
   ```
   - Gateway adds custom header: `X-Service: SchoolService`
   - Sets response status to 401 (Unauthorized) - for testing purposes

6. **Forward to Service**
   - Request forwarded to School Service
   - School Service processes request

7. **Response Flow** (Backward)
   ```
   School Service → API Gateway → Client
   ```
   - Response includes custom header
   - Status code: 401 (as configured)

#### **Visual Flow Diagram**
```
Client Request (localhost:8085/school/students)
    ↓
API Gateway (Port 8085)
    ↓
[Route Matching: /school/**]
    ↓
[Query Eureka Server]
    ↓
Eureka Returns: SCHOOL-SERVICE @ localhost:8082
    ↓
[Apply Filters]
    ├─ Add Header: X-Service=SchoolService
    └─ Set Status: 401
    ↓
Forward to School Service (localhost:8082/school/students)
    ↓
School Service Processes Request
    ↓
Response Back Through Gateway
    ↓
Client Receives Response (with custom header & status)
```

---

## 🗂️ Where Spring Security is Used

### **1. Weather Service** ✅ (Full Implementation)
**Location**: `weather-service/src/main/java/com/codesnippet/weather_service/`

**Components**:
- `config/SecurityConfig.java` - Main security configuration
- `filter/JwtAuthFilter.java` - JWT authentication filter
- `service/CustomUserDetailsService.java` - User details loading
- `controller/AuthController.java` - Authentication endpoint
- `entity/Users.java` - User entity
- `entity/Role.java` - Role entity
- `entity/Permissions.java` - Permissions enum

**Features Implemented**:
✅ JWT Authentication
✅ Role-Based Access Control
✅ Permission-Based Authorization
✅ Password Encryption (BCrypt)
✅ Custom Authentication Filter
✅ UserDetailsService
✅ Method Security

---

### **2. School Service** ⚠️ (Partial - OAuth2 Client)
**Location**: `school-service/src/main/java/com/example/schoolservice/`

**Components**:
- `interceptor/FeignOAuth2Config.java` - OAuth2 Feign client config
- `interceptor/FeignInterceptorConfig.java` - Custom Feign interceptor

**Features**:
- OAuth2 token propagation for Feign clients
- Custom request headers via interceptors
- *Note*: No endpoint-level security configured

---

### **3. Other Services** ❌ (No Security)
- **API Gateway**: No security (acts as router only)
- **Student Service**: No security
- **Beer API**: No security
- **Eureka Server**: No security (dashboard is open)

---

## 🛠️ Prerequisites

- **Java**: 17 or higher
- **Maven**: 3.6+
- **IDE**: IntelliJ IDEA / Eclipse / VS Code
- **Git**: For version control

---

## 🚀 Getting Started

### **1. Clone the Repository**
```bash
git clone https://github.com/REENA1998/Spring_tutorials.git
cd Spring_tutorials
```

### **2. Start Services in Order**

#### **Step 1: Start Eureka Server**
```bash
cd eureka-server
mvn spring-boot:run
```
- Access: http://localhost:8761
- Wait for Eureka dashboard to load

#### **Step 2: Start Microservices**
Open separate terminals for each:

```bash
# Terminal 2 - API Gateway
cd api-gateway
mvn spring-boot:run

# Terminal 3 - Student Service
cd student-service
mvn spring-boot:run

# Terminal 4 - School Service
cd school-service
mvn spring-boot:run

# Terminal 5 - Beer API
cd beer-api
mvn spring-boot:run

# Terminal 6 - Weather Service
cd weather-service
mvn spring-boot:run
```

### **3. Verify Services**
Check Eureka Dashboard: http://localhost:8761

You should see all services registered:
- API-GATEWAY
- STUDENT-SERVICE
- SCHOOL-SERVICE
- BEER-API
- WEATHER-SERVICE

---

## 🧪 Testing the Services

### **1. Test API Gateway Routing**

**Student Service via Gateway:**
```bash
curl http://localhost:8085/fetch-students/
```

**School Service via Gateway:**
```bash
curl -v http://localhost:8085/school/students
# Notice: Response status 401 (as configured)
# Header: X-Service: SchoolService
```

### **2. Test Weather Service Security**

**Authenticate (Get JWT Token):**
```bash
curl -X POST http://localhost:8080/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Access Protected Endpoint:**
```bash
# Replace YOUR_JWT_TOKEN with actual token from above
curl http://localhost:8080/weather \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### **3. Test Direct Service Access**

**Student Service Direct:**
```bash
curl http://localhost:8081/fetch-students/
```

**School Service Direct:**
```bash
curl http://localhost:8082/school/students
```

---

## 📖 Learning Path

### **Beginner**
1. Start with Eureka Server (Service Discovery)
2. Create a simple microservice
3. Register service with Eureka

### **Intermediate**
4. Add API Gateway
5. Configure routes and filters
6. Implement Feign Client
7. Add custom interceptors

### **Advanced**
8. Implement JWT Authentication (Weather Service)
9. Add Role-Based Access Control
10. Configure Method-level Security
11. Implement OAuth2 client

---

## 📁 Project Structure
```
micro_tuts/
├── eureka-server/          # Service Discovery
├── api-gateway/            # API Gateway with routing
├── student-service/        # Student management service
├── school-service/         # School management + Feign client
├── beer-api/               # Simple REST API demo
├── weather-service/        # Advanced service with full security
├── .gitignore              # Git ignore patterns
└── README.md               # This file
```

---

## 🔧 Configuration Highlights

### **Eureka Server** (application.properties)
```properties
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

### **API Gateway** (application.yml)
```yaml
server:
  port: 8085

spring:
  cloud:
    gateway:
      routes:
        - id: school_route
          uri: lb://SCHOOL-SERVICE
          predicates:
            - Path=/school/**
          filters:
            - SetStatus=401
            - AddResponseHeader=X-Service, SchoolService
```

### **Weather Service Security** (SecurityConfig.java)
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/authenticate").permitAll()
    .anyRequest().authenticated()
)
```

---

## 🎯 Key Takeaways

1. **Service Discovery**: Automatic service registration and discovery with Eureka
2. **API Gateway**: Single entry point with intelligent routing
3. **Load Balancing**: Automatic load balancing using `lb://` protocol
4. **Feign Clients**: Simplified inter-service REST communication
5. **JWT Security**: Stateless authentication for microservices
6. **RBAC**: Fine-grained access control with roles and permissions
7. **Filters & Interceptors**: Request/response manipulation
8. **Configuration**: Externalized configuration for different environments

---

## 🤝 Contributing
Feel free to fork this repository and submit pull requests!

---

## 📧 Contact
**Author**: REENA1998  
**Repository**: [Spring_tutorials](https://github.com/REENA1998/Spring_tutorials)

---

## 📄 License
This project is for educational purposes.

---

## 🌟 Next Steps
- Add distributed tracing (Sleuth + Zipkin)
- Implement Circuit Breaker (Resilience4j)
- Add centralized configuration (Spring Cloud Config)
- Implement API rate limiting
- Add monitoring (Actuator + Prometheus + Grafana)
- Implement event-driven communication (Kafka/RabbitMQ)

---

**Happy Learning! 🚀**

