# Weather Alert System API Documentation

## Overview
The Weather Alert System provides a RESTful API for managing weather alerts and user notifications. This document outlines all available endpoints and their usage.

## Base URL
- Development: `http://localhost:8080/api`
- Production: `https://your-domain.com/api`

## Authentication
The API uses Basic Authentication for protected endpoints:
- Username: `admin`
- Password: Set via `ADMIN_PASSWORD` environment variable

## Endpoints

### User Management

#### Register User
```
POST /api/users/register
Content-Type: application/json

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

#### Get User by ID
```
GET /api/users/{userId}
Authorization: Basic admin:password
```

#### Update User
```
PUT /api/users/{userId}
Authorization: Basic admin:password
Content-Type: application/json

{
  "name": "John Smith",
  "phoneNumber": "+1987654321",
  "emailNotificationsEnabled": false,
  "smsNotificationsEnabled": true,
  "alertPreferences": ["HEATWAVE", "HURRICANE"],
  "locations": ["Miami", "Boston"]
}
```

#### Get All Users
```
GET /api/users
Authorization: Basic admin:password
```

#### Delete User
```
DELETE /api/users/{userId}
Authorization: Basic admin:password
```

### Weather Information

#### Get Current Weather
```
GET /api/weather/current/{location}

Example: GET /api/weather/current/London
```

#### Get Weather Alerts
```
GET /api/weather/alerts/{location}

Example: GET /api/weather/alerts/New York
```

### Alert Management

#### Get User Alerts
```
GET /api/alerts/user/{userId}
Authorization: Basic admin:password
```

#### Trigger Manual Weather Check
```
POST /api/alerts/check
Authorization: Basic admin:password
```

#### Cleanup Expired Alerts
```
POST /api/alerts/cleanup
Authorization: Basic admin:password
```

## Response Examples

### User Registration Response
```json
{
  "id": "64f1a2b3c4d5e6f7g8h9i0j1",
  "email": "user@example.com",
  "name": "John Doe",
  "phoneNumber": "+1234567890",
  "emailNotificationsEnabled": true,
  "smsNotificationsEnabled": true,
  "alertPreferences": ["SEVERE_WEATHER", "STORM"],
  "locations": ["New York", "London"],
  "createdAt": "2023-09-01T10:00:00",
  "updatedAt": "2023-09-01T10:00:00"
}
```

### Weather Data Response
```json
{
  "location": "London",
  "temperature": 22.5,
  "humidity": 65.0,
  "windSpeed": 15.2,
  "windDirection": "180",
  "pressure": 1013.25,
  "description": "partly cloudy",
  "mainCondition": "Clouds",
  "visibility": 10000.0,
  "uvIndex": 0.0,
  "precipitationChance": 0.0
}
```

### Alert Response
```json
{
  "id": "64f1a2b3c4d5e6f7g8h9i0j2",
  "userId": "64f1a2b3c4d5e6f7g8h9i0j1",
  "alertType": "STORM",
  "location": "London",
  "title": "Storm Alert for London",
  "message": "Severe storm detected in London. Current conditions: thunderstorm, Temperature: 18.5°C, Wind: 35.2 km/h. Please take necessary precautions.",
  "severity": "HIGH",
  "status": "SENT",
  "createdAt": "2023-09-01T15:30:00",
  "sentAt": "2023-09-01T15:30:15",
  "expiresAt": "2023-09-01T21:30:00"
}
```

## Error Responses

### Validation Error
```json
{
  "timestamp": "2023-09-01T10:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "path": "/api",
  "validationErrors": {
    "email": "Email should be valid",
    "name": "Name is required"
  }
}
```

### Service Unavailable
```json
{
  "timestamp": "2023-09-01T10:00:00",
  "status": 503,
  "error": "Service Unavailable",
  "message": "External weather service is currently unavailable",
  "path": "/api/weather"
}
```

## Alert Types
- `SEVERE_WEATHER`: General severe weather conditions
- `STORM`: Thunderstorms and severe storms
- `HEATWAVE`: Extreme high temperatures
- `FLOOD`: Flooding conditions
- `SNOW`: Heavy snow conditions
- `TORNADO`: Tornado warnings
- `HURRICANE`: Hurricane alerts
- `HIGH_WIND`: High wind conditions
- `FREEZING_RAIN`: Freezing rain alerts
- `GENERAL_WARNING`: General weather warnings

## Status Codes
- `200 OK`: Successful request
- `201 Created`: Resource created successfully
- `204 No Content`: Successful request with no content
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Authentication required
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error
- `503 Service Unavailable`: External service unavailable
