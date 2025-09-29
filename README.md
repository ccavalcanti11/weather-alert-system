# Weather Alert Notification System

![Java](https://img.shields.io/badge/Java-21-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.3-brightgreen.svg)
![MongoDB](https://img.shields.io/badge/MongoDB-Enabled-green.svg)
![Redis](https://img.shields.io/badge/Redis-Caching-red.svg)
![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)
![Build](https://img.shields.io/badge/Build-Passing-success.svg)

## Real-time Weather Alert System with Multi-Channel Notifications

Welcome to the **Weather Alert Notification System** — a showcase of modern Java 21 development using Spring Boot that demonstrates real-time weather monitoring and intelligent alert distribution. This project highlights my expertise in building scalable, reactive systems with external API integration, scheduled processing, and multi-channel communication.

This comprehensive system monitors weather conditions across multiple locations, processes real-time data from external APIs, and delivers personalized alerts via email and SMS, showcasing enterprise-grade development patterns with functional programming approaches.

## 📖 Table of Contents

- [🚀 Tech Stack & Architecture](#-tech-stack--architecture)
  - [Core Technologies](#core-technologies)
  - [External Integrations](#external-integrations)
  - [Testing & Quality](#testing--quality)
  - [Build & Deployment](#build--deployment)
- [🌦️ System Features & Business Logic](#️-system-features--business-logic)
  - [🔄 Real-time Weather Monitoring](#-real-time-weather-monitoring)
  - [👤 User Management](#-user-management)
  - [📱 Multi-Channel Notifications](#-multi-channel-notifications)
  - [⚡ Intelligent Alert Processing](#-intelligent-alert-processing)
  - [🔍 Advanced Filtering & Caching](#-advanced-filtering--caching)
- [🏗️ Architecture & Design Patterns](#️-architecture--design-patterns)
  - [Clean Architecture Layers](#clean-architecture-layers)
  - [Key Design Decisions](#key-design-decisions)
- [📂 Project Structure](#-project-structure)
- [🛠️ API Endpoints Overview](#️-api-endpoints-overview)
  - [User Management API](#user-management-api-apiusers)
  - [Weather Data API](#weather-data-api-apiweather)
  - [Alert Management API](#alert-management-api-apialerts)
- [🧪 Testing Strategy](#-testing-strategy)
  - [Unit Tests](#unit-tests)
  - [Integration Tests](#integration-tests)
  - [Test Coverage](#test-coverage)
- [🚀 Performance & Scalability Features](#-performance--scalability-features)
  - [Caching Strategy](#caching-strategy)
  - [Scheduled Processing](#scheduled-processing)
  - [External API Optimization](#external-api-optimization)
- [📖 API Documentation](#-api-documentation)
  - [Interactive Documentation](#interactive-documentation)
  - [Request/Response Examples](#requestresponse-examples)
- [🛠️ Quick Start Guide](#️-quick-start-guide)
  - [Prerequisites](#prerequisites)
  - [Docker Compose Setup](#option-1-docker-compose-recommended)
  - [Local Development](#option-2-local-development)
  - [Accessing the Application](#accessing-the-application)
- [🐳 Docker Configuration](#-docker-configuration)
  - [Multi-Service Setup](#multi-service-setup)
  - [Environment Configuration](#environment-configuration)
- [🔍 Monitoring & Observability](#-monitoring--observability)
  - [Health Checks](#health-checks)
  - [Available Endpoints](#available-endpoints)
- [🏆 Best Practices Demonstrated](#-best-practices-demonstrated)
  - [Code Quality](#code-quality)
  - [Security Considerations](#security-considerations)
  - [Production Readiness](#production-readiness)
- [📬 Contact & Collaboration](#-contact--collaboration)

## 🚀 Tech Stack & Architecture

### Core Technologies
- **Java 21** - Latest LTS with modern language features and functional programming
- **Spring Boot 3.3.3** - Enterprise-grade framework with auto-configuration
- **Spring Data MongoDB** - NoSQL document database for user profiles and alerts
- **Spring Data Redis** - High-performance caching for weather data
- **Spring Scheduler** - Automated weather monitoring and alert processing
- **Spring Security** - Basic authentication and endpoint protection
- **Spring Validation** - Comprehensive input validation with Jakarta Bean Validation

### External Integrations
- **OpenWeatherMap API** - Real-time weather data and severe weather alerts
- **Twilio API** - SMS notification delivery service
- **Mailgun API** - Professional email delivery service
- **Spring WebClient** - Reactive HTTP client for external API calls

### Testing & Quality
- **JUnit 5** - Modern testing framework with parameterized tests
- **Mockito** - Advanced mocking for service layer testing
- **Spring Boot Test** - Comprehensive integration testing support
- **Embedded MongoDB & Redis** - Isolated testing environments

### Build & Deployment
- **Gradle** - Modern build automation with dependency management
- **Docker & Docker Compose** - Containerization and multi-service orchestration
- **Spring Boot Actuator** - Production monitoring and health checks

## 🌦️ System Features & Business Logic

### 🔄 Real-time Weather Monitoring
- **Scheduled weather checks** every 5 minutes across all user locations
- **Severe weather detection** with configurable alert thresholds
- **Multi-location monitoring** for each user with personalized preferences
- **Weather condition analysis** for storms, hurricanes, heatwaves, floods, and more
- **Intelligent cooldown periods** to prevent alert spam (3-hour minimum between similar alerts)
- **Automatic alert expiration** with cleanup tasks every 6 hours

### 👤 User Management
- **Comprehensive user registration** with email and phone validation
- **Notification preferences** for email and SMS channels
- **Alert type customization** (severe weather, storms, heatwaves, floods, etc.)
- **Multi-location tracking** for users monitoring multiple cities
- **Profile management** with update and deletion capabilities
- **Email-based unique identification** with duplicate prevention

### 📱 Multi-Channel Notifications
- **Email notifications** via Mailgun with professional templates
- **SMS alerts** via Twilio for urgent weather conditions
- **Customizable alert messages** with location-specific weather details
- **Template-based formatting** with consistent branding
- **Delivery status tracking** with success/failure logging
- **Channel preference management** (email-only, SMS-only, or both)

### ⚡ Intelligent Alert Processing
- **Business rule validation** before alert creation
- **Alert severity classification** (LOW, MEDIUM, HIGH, CRITICAL)
- **Weather condition mapping** to appropriate alert types
- **Duplicate prevention** with location and time-based filtering
- **Status tracking** (PENDING, SENT, FAILED, EXPIRED)
- **Automated retry logic** for failed notification deliveries

### 🔍 Advanced Filtering & Caching
- **Redis-based caching** for weather API responses (reduces external calls)
- **User preference filtering** to send only relevant alerts
- **Geographic-based processing** with location-specific weather data
- **Alert history management** with user-specific retrieval
- **Cache invalidation strategies** for real-time data freshness

## 🏗️ Architecture & Design Patterns

### Clean Architecture Layers
```
┌─────────────────────────────────────────┐
│           REST Controllers              │  ← API Layer (User, Weather, Alert)
├─────────────────────────────────────────┤
│             Service Layer               │  ← Business Logic (Functional Approach)
├─────────────────────────────────────────┤
│           Repository Layer              │  ← Data Access (MongoDB)
├─────────────────────────────────────────┤
│        External Integrations           │  ← Weather API, Twilio, Mailgun
├─────────────────────────────────────────┤
│              Domain Models              │  ← Entities with Lombok @Data/@Builder
└─────────────────────────────────────────┘
```

### Key Design Decisions
- **Functional Programming** approach with Java 21 features (streams, optionals)
- **Repository Pattern** for data access abstraction
- **Service Layer** with business logic encapsulation
- **DTO Pattern** with Lombok builders and validation annotations
- **Scheduled Processing** for automated weather monitoring
- **Caching Strategy** for external API optimization
- **Global Exception Handling** for consistent error responses
- **Builder Pattern** throughout DTOs and entities for immutability

## 📂 Project Structure

```
src/
├── main/
│   ├── java/com/weather/alert/system/
│   │   ├── controller/          # REST endpoints
│   │   │   ├── UserController.java
│   │   │   ├── WeatherController.java
│   │   │   └── AlertController.java
│   │   ├── service/             # Business logic
│   │   │   ├── UserService.java
│   │   │   ├── WeatherService.java
│   │   │   ├── AlertService.java
│   │   │   └── NotificationService.java
│   │   ├── repository/          # Data access
│   │   │   ├── UserRepository.java
│   │   │   └── AlertRepository.java
│   │   ├── model/               # Domain entities
│   │   │   ├── User.java
│   │   │   ├── Alert.java
│   │   │   ├── WeatherCondition.java
│   │   │   └── AlertType.java
│   │   ├── dto/                 # Data transfer objects
│   │   │   ├── UserRegistrationDto.java
│   │   │   ├── AlertDto.java
│   │   │   └── WeatherConditionDto.java
│   │   ├── config/              # Configuration
│   │   │   ├── RedisConfig.java
│   │   │   └── SecurityConfig.java
│   │   └── exception/           # Error handling
│   │       ├── GlobalExceptionHandler.java
│   │       └── ErrorResponse.java
│   └── resources/
│       ├── application.properties
│       ├── application-prod.properties
│       └── application-test.properties
└── test/
    └── java/com/weather/alert/system/
        └── service/             # Unit tests
            ├── UserServiceTest.java
            ├── WeatherServiceTest.java
            └── AlertServiceTest.java
```

## 🛠️ API Endpoints Overview

### User Management API (`/api/users`)
- `POST /api/users/register` - Register new user with preferences
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user information and preferences
- `DELETE /api/users/{id}` - Remove user account
- `GET /api/users` - List all users (admin)
- `GET /api/users/email/{email}` - Find user by email

### Weather Data API (`/api/weather`)
- `GET /api/weather/current/{location}` - Get current weather conditions
- `GET /api/weather/alerts/{location}` - Get severe weather alerts for location

### Alert Management API (`/api/alerts`)
- `GET /api/alerts/user/{userId}` - Get user's alert history
- `POST /api/alerts/check` - Manually trigger weather check (admin)
- `POST /api/alerts/cleanup` - Clean up expired alerts (admin)

## 🧪 Testing Strategy

### Unit Tests
- **Service Layer Testing** with Mockito for external dependencies
- **Business Logic Validation** for alert processing rules
- **Weather Service Testing** with mocked API responses
- **Notification Service Testing** for email and SMS delivery
- **Repository Testing** with embedded MongoDB

### Integration Tests
- **End-to-end API Testing** with real database instances
- **External API Integration** testing with test endpoints
- **Caching Integration** testing with embedded Redis
- **Scheduled Task Testing** for automated processing

### Test Coverage
- **Comprehensive unit tests** for all service methods
- **Edge case testing** for weather condition mapping
- **Error condition testing** for external API failures
- **Validation testing** for input data constraints

## 🚀 Performance & Scalability Features

### Caching Strategy
- **Redis-based caching** for weather API responses (15-minute TTL)
- **Location-based cache keys** for efficient data retrieval
- **Cache warming** during scheduled weather checks
- **Automatic cache invalidation** for data freshness

### Scheduled Processing
- **Parallel processing** of user locations using streams
- **Configurable scheduling intervals** (5-minute default)
- **Error isolation** to prevent single failures from affecting others
- **Batch processing** for multiple users and locations

### External API Optimization
- **Connection pooling** for HTTP clients
- **Request throttling** to respect API rate limits
- **Retry logic** for transient failures
- **Circuit breaker pattern** for service resilience

## 📖 API Documentation

### Interactive Documentation
Comprehensive API documentation is available through the built-in endpoints and the separate API documentation file.

### Request/Response Examples

#### User Registration
```json
POST /api/users/register
{
  "email": "user@example.com",
  "name": "John Doe",
  "phoneNumber": "+1234567890",
  "emailNotificationsEnabled": true,
  "smsNotificationsEnabled": true,
  "alertPreferences": ["SEVERE_WEATHER", "STORM"],
  "locations": ["New York", "London"]
}
```

#### Weather Alert Response
```json
{
  "id": "alert123",
  "userId": "user456",
  "alertType": "STORM",
  "location": "London",
  "title": "Storm Alert for London",
  "message": "Severe storm detected in London. Current conditions: thunderstorm, Temperature: 18.5°C, Wind: 35.2 km/h.",
  "severity": "HIGH",
  "status": "SENT",
  "createdAt": "2023-09-01T15:30:00",
  "sentAt": "2023-09-01T15:30:15"
}
```

## 🛠️ Quick Start Guide

### Prerequisites
- **Java 21** or later
- **Docker & Docker Compose** (for containerized setup)
- **API Keys** for OpenWeatherMap, Twilio, and Mailgun

### Option 1: Docker Compose (Recommended)
```bash
# Clone the repository
git clone https://github.com/yourusername/weather-alert-system.git
cd weather-alert-system

# Copy environment template and configure
cp .env.template .env
# Edit .env with your API keys

# Start all services
chmod +x build-and-run.sh
./build-and-run.sh

# Or manually:
docker-compose up --build
```

### Option 2: Local Development
```bash
# Start MongoDB and Redis locally
# Configure environment variables in application.properties
./gradlew bootRun
```

### Accessing the Application
- **API Base URL**: http://localhost:8080/api
- **Health Check**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics

## 🐳 Docker Configuration

### Multi-Service Setup
The application uses Docker Compose to orchestrate:
- **MongoDB** (port 27017) - User data and alert storage
- **Redis** (port 6379) - Weather data caching
- **Weather Alert API** (port 8080) - Main application

### Environment Configuration
```yaml
services:
  weather-alert-system:
    build: .
    environment:
      - MONGO_URI=mongodb://mongodb:27017/weather_alert_db
      - REDIS_HOST=redis
      - WEATHER_API_KEY=${WEATHER_API_KEY}
      - TWILIO_ACCOUNT_SID=${TWILIO_ACCOUNT_SID}
      - MAILGUN_API_KEY=${MAILGUN_API_KEY}
```

## 🔍 Monitoring & Observability

### Health Checks
- **Application health** via Spring Boot Actuator
- **Database connectivity** monitoring for MongoDB
- **Cache system** health checks for Redis
- **External API** connectivity validation

### Available Endpoints
- `/actuator/health` - Overall system health
- `/actuator/info` - Application information
- `/actuator/metrics` - Performance metrics

## 🏆 Best Practices Demonstrated

### Code Quality
- **Functional programming** with Java 21 streams and optionals
- **Immutable DTOs** using Lombok @Builder pattern
- **Clean separation** of concerns across layers
- **Comprehensive error handling** with custom exceptions

### Security Considerations
- **Input validation** with Jakarta Bean Validation
- **Basic authentication** for admin endpoints
- **Environment variable** externalization for sensitive data
- **Secure API integration** with proper credential management

### Production Readiness
- **Multi-environment configuration** (dev, test, prod)
- **Comprehensive logging** with proper log levels
- **Graceful error handling** and recovery mechanisms
- **Container orchestration** for scalable deployment
- **Health monitoring** and metrics collection

## 📬 Contact & Collaboration

I'm passionate about building resilient, scalable systems and modern Java development. This project demonstrates my expertise in real-time data processing, external API integration, and enterprise-grade application architecture.

**Let's connect:**
- **LinkedIn**: [Carlos Gustavo Cavalcanti](https://www.linkedin.com/in/carlos-gustavo-cavalcanti/)
- **Email**: dev1carloscavalcanti@gmail.com
- **GitHub**: Feel free to explore more of my projects!

---

⭐ **If you found this project valuable, please consider starring the repository!** ⭐

This project showcases modern Java development practices and serves as an excellent reference for:
- **Real-time data processing** with scheduled tasks
- **External API integration** with caching strategies
- **Multi-channel notification systems**
- **Functional programming** in Java 21
- **Docker containerization** for microservices
- **Enterprise-grade error handling** and monitoring
