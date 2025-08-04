# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a full-stack team matching application with Spring Boot backend and React frontend:

**Backend (Spring Boot 3.5.3)**:
- OAuth2 authentication (Google) with JWT tokens
- Real-time chat using WebSockets 
- Team creation and membership management
- Dashboard functionality
- PostgreSQL database (prod) / H2 (local)
- Hazelcast caching
- Docker containerization

**Frontend (React + TypeScript + Vite)**:
- Modern React 19 with TypeScript
- Shadcn/ui components with Radix UI primitives
- Tailwind CSS for styling
- Zustand for state management
- React Hook Form with Zod validation
- Axios for API communication
- Socket.io for real-time features

## Architecture

### Backend Architecture
The backend follows standard Spring Boot layered architecture:
- **Controllers**: Handle HTTP requests and WebSocket connections
- **Services**: Business logic layer
- **Repositories (DAO)**: Data access layer using Spring Data JPA
- **Entities**: JPA entities representing database tables
- **DTOs**: Data transfer objects for API requests/responses
- **Config**: Security, CORS, WebSocket, and caching configuration

Key backend components:
- **Authentication**: JWT-based with OAuth2 (Google) integration in `auth/` package
- **Chat System**: WebSocket-based real-time messaging in `chat/` package
- **Team Management**: Team creation, invitations, and membership in `team/` package
- **User Management**: User profiles and preferences in `user/` package
- **Dashboard**: Analytics and summary data in `dashboard/` package

### Frontend Architecture
The frontend follows feature-based architecture:
- **Components**: Reusable UI components organized by type (common, layout, features, ui)
- **Pages**: Route-based page components
- **Store**: Zustand state management (user, match, chat stores)
- **API**: Axios-based API layer with service separation
- **Hooks**: Custom React hooks for reusable logic
- **Types**: TypeScript definitions for data models
- **Utils**: Utility functions and constants

OAuth2 login flow: `/users/login/google` endpoint initiates Google OAuth, returns JWT on success.

## Development Commands

### Backend (Spring Boot)
```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Run with coverage report
./gradlew test jacocoTestReport

# Run application locally
./gradlew bootRun

# Run with local profile (H2 database)
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun

# Run specific test class
./gradlew test --tests "com.example.demo.team.controller.TeamControllerTest"
```

### Frontend (React)
```bash
# Install dependencies
cd front_end && npm install

# Start development server
npm run dev

# Build for production
npm run build

# Lint code
npm run lint

# Preview production build
npm run preview
```

### Full-Stack Development
```bash
# Run both backend and frontend with Docker
docker-compose up --build

# Run in background
docker-compose up -d

# Development mode with hot reload
cd front_end && docker-compose -f docker-compose.dev.yml up
```

## Configuration Profiles

- **local**: Uses H2 in-memory database, H2 console enabled at `/h2-console`
- **prod**: Uses PostgreSQL database, optimized for production

Database URLs:
- Local: `jdbc:h2:~/test` 
- Prod: `jdbc:postgresql://postgres:5432/team_search`

## Key Dependencies

- Spring Boot 3.5.3 (Web, Security, Data JPA, WebSocket, OAuth2 Client)
- JWT: `io.jsonwebtoken:jjwt-*:0.11.5`
- Database: PostgreSQL (prod), H2 (local)
- Caching: Hazelcast 5.5.0
- Monitoring: Actuator with Prometheus metrics
- Testing: JUnit 5, Spring Security Test

## Security Configuration

The application uses dual security configurations:
- `SecurityConfig.java`: Development configuration
- `ProdSecurityConfig.java`: Production configuration with enhanced security

JWT authentication is handled through:
- `JwtFilter`: Validates JWT tokens on requests
- `JwtUtil`: Creates and validates JWT tokens
- OAuth2 handlers: Manage authentication success/failure flows

## Important Notes

- The application uses Java 17
- WebSocket connections are configured in `WebSocketConfig`
- CORS is configured in `CorsConfig` 
- Chat functionality requires WebSocket connection to `/ws`
- All tests use H2 database regardless of active profile
- Jacoco coverage reports are generated in `build/reports/jacoco/test/html/`