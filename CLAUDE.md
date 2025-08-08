# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Match SSAFY** - A full-stack team matching platform specifically designed for SSAFY (Samsung Software Academy For Youth) trainees. This monorepo consists of three main services: Spring Boot backend, React frontend, and FastAPI recommendation system.

### SSAFY-Specific Context
- Users are categorized by major/non-major tracks (전공자/비전공자)
- Class system with numbered groups (1반-12반)
- Skill matching based on SSAFY curriculum (Java, Python, embedded, etc.)
- Project-based team formation with deadlines

**Backend (Spring Boot 3.5.3 + Java 17)**:
- Google OAuth2 authentication with JWT token management
- Real-time WebSocket chat system (STOMP protocol) with Hazelcast distributed caching
- Advanced team management with membership requests/invitations
- Comprehensive user profiling with enum-based categorization
- Dashboard analytics for team and user metrics
- Multi-environment setup: H2 (local), PostgreSQL (production)
- Swagger/OpenAPI 3 documentation at `/swagger-ui.html`
- Docker containerization

**Frontend (React 19 + TypeScript + Vite)**:
- Modern React 19 with TypeScript and SWC for fast compilation
- Shadcn/ui design system with Radix UI accessibility primitives
- Tailwind CSS with responsive design and custom gradient themes
- Zustand for lightweight state management (user, match, chat stores)
- React Hook Form with Zod validation for form handling
- Axios with interceptors for API communication
- Multi-step profile setup with progress tracking
- Mock data integration for development

**RecSys (FastAPI + Python)**:
- AI-powered team-person recommendation system
- Machine learning algorithms for skill and preference matching
- RESTful API for integration with Spring Boot backend
- Scikit-learn, Pandas, NumPy for data processing and ML models

## Architecture

### Backend Architecture (back_end/)
The backend follows Spring Boot's layered architecture with domain-driven design:

- **Controllers**: REST API endpoints with comprehensive Swagger documentation (`*Controller.java`)
- **Services**: Business logic layer with transaction management (`*Service.java`)
- **Repositories (DAO)**: Spring Data JPA repositories with custom queries (`*Repository.java`)
- **Entities**: JPA entities with proper relationships and constraints (`*.java` in entity packages)
- **DTOs**: Validated request/response objects (`*Request.java`, `*Response.java`)
- **Enums**: Type-safe categorization (`user/Enum/` package)
- **Config**: Security, CORS, WebSocket, Swagger, Hazelcast configuration

Key modules:
- `auth/`: JWT + OAuth2 Google, custom authorization resolvers, success/failure handlers
- `chat/`: WebSocket STOMP messaging, room management, message persistence
- `team/`: Advanced team management with membership requests, invitations, leader management
- `user/`: Comprehensive user profiles with SSAFY-specific fields, skill matching
- `dashboard/`: Analytics, user counts, team statistics

### Frontend Architecture (front_end/src/)
Feature-based organization with modern React patterns:

- `components/`: Component library with clear separation
  - `common/`: Reusable components (Button, Input, Modal) - shared UI elements
  - `layout/`: Layout components (Header, Footer, Sidebar) - page structure
  - `features/`: Domain-specific components organized by feature
    - `auth/`: GoogleSignInButton with OAuth integration
    - `home/`: TeamSection, DeveloperSection, modals for main dashboard
    - `profile/`: Multi-step profile setup (PositionSelector, SkillSelector, etc.)
    - `chat/`: Real-time messaging components
    - `matching/`: Team/developer search and filtering
  - `ui/`: Shadcn/ui component library (40+ components)
- `pages/`: Route-based page components with React Router
  - Multi-step flows (ProfileSetup, MakeTeam)
  - Authentication pages (Login, Signup, OAuthCallback)
  - Main application pages (Home, Chat, Matching, Profile)
- `store/`: Minimal Zustand stores for global state
- `api/`: Axios instance with environment-based baseURL configuration
- `hooks/`: Custom React hooks (useAuth, useSocket, useMatch, use-toast)
- `types/`: TypeScript definitions for User, Match, Chat with SSAFY-specific fields
- `data/`: Mock data for development and prototyping
- `services/`: Business logic and external service integrations
- `utils/`: Helper functions, constants, validation schemas

### RecSys Architecture (RecSys/)
FastAPI-based recommendation service with machine learning:

- **`main.py`**: FastAPI application entry point with CORS and logging setup
- **`app/api/routes.py`**: REST API endpoints for recommendation services
- **`app/core/config.py`**: Configuration management, CORS setup, logging
- **`app/schemas/models.py`**: Pydantic models for request/response validation
- **`app/services/recommender.py`**: Machine learning algorithms and recommendation logic
- **`requirements.txt`**: Python dependencies (FastAPI, scikit-learn, pandas, numpy)

## Development Commands

### Backend Development
```bash
# Navigate to backend directory
cd back_end

# Build project
./gradlew build

# Run tests with coverage
./gradlew test jacocoTestReport

# Run application locally (H2 database)
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun

# Run with production profile (PostgreSQL)
SPRING_PROFILES_ACTIVE=prod ./gradlew bootRun

# Run specific test class
./gradlew test --tests "com.example.demo.team.controller.TeamControllerTest"
```

### Frontend Development
```bash
# Navigate to frontend directory
cd front_end

# Install dependencies
npm install

# Start development server (http://localhost:3000)
npm run dev

# Build for production
npm run build

# Lint code
npm run lint

# Preview production build
npm run preview
```

### RecSys Development (AI/ML Service)
```bash
# Navigate to RecSys directory
cd RecSys

# Install Python dependencies
pip install -r requirements.txt

# Run FastAPI development server
python main.py
# or
uvicorn main:app --reload
```

### Full-Stack Development
```bash
# Run both services with Docker (backend: :8081, frontend: :8080)
docker-compose up --build

# Production deployment with optimized images
docker-compose -f docker-compose.release.yml up --build

# Development mode with hot reload
cd front_end && docker-compose -f docker-compose.dev.yml up

# Run in background
docker-compose up -d
```

## Configuration & Environment

### Spring Profiles
- **local**: H2 in-memory database, H2 console at `/h2-console`, relaxed CORS
- **prod**: PostgreSQL database, production security settings, restricted CORS

### Database Configuration
- Local: `jdbc:h2:~/test` (H2 console enabled)
- Production: `jdbc:postgresql://postgres:5432/team_search`
- Schema initialization: `schema-mysql.sql` in resources

### Security & Authentication
OAuth2 flow: Frontend redirects to `/users/login/google` → Google OAuth → JWT token returned
- JWT tokens handled by `JwtFilter` and `JwtUtil`
- Security configs: `SecurityConfig.java` (dev) and `ProdSecurityConfig.java` (prod)
- OAuth2 success/failure handlers manage authentication flow

## Key Dependencies & Technologies

**Backend**:
- Spring Boot 3.5.3 (Web, Security, Data JPA, WebSocket, OAuth2 Client, Actuator)
- JWT: `io.jsonwebtoken:jjwt-*:0.11.5`
- Database: PostgreSQL (prod), H2 (local)
- Caching: Hazelcast 5.5.0 for distributed caching
- Monitoring: Actuator with Prometheus metrics
- Testing: JUnit 5, Spring Security Test
- Java 17 runtime

**Frontend**:
- React 19 with TypeScript 5.8
- Vite 7.0 with SWC for fast compilation
- Shadcn/ui + Radix UI primitives for components
- Tailwind CSS 3.4 with custom configuration
- Zustand 5.0 for state management
- React Hook Form 7.61 + Zod 4.0 for forms
- Axios 1.11 for API calls
- React Router 7.7 for routing

**RecSys (AI/ML)**:
- FastAPI 0.116.1 for REST API framework
- Pydantic 2.11.7 for data validation and serialization
- Scikit-learn 1.7.1 for machine learning algorithms
- Pandas 2.3.1 + NumPy 2.3.2 for data processing
- Uvicorn 0.35.0 ASGI server for production deployment

## Core Features & Workflows

### User Registration & Profile Setup
1. Google OAuth2 authentication via `/users/login/google`
2. Multi-step profile creation:
   - Step 1: Position selection, tech stack, self-introduction
   - Step 2: Project preferences, personal preferences, certifications
3. SSAFY-specific information: major/non-major track, class number (1-12)

### Team Management System
- **Team Creation**: Leaders create teams with project descriptions and preferences
- **Membership Requests**: Two-way system (JOIN/INVITE) with approval workflows
- **Team Types**: Different request flows for joining vs. being invited
- **Advanced Search**: Filter teams/users by skills, positions, project goals

### Real-Time Chat System
- **WebSocket Architecture**: `/ws` endpoint with STOMP protocol
- **Message Types**: Group chat (`/topic/chat/room/{id}`) vs 1:1 chat (`/queue/chat/room/{id}`)
- **Integration**: Automatic chat room creation when teams are formed
- **Persistence**: Messages stored in database with user/room relationships

### API Structure
- **User Endpoints**: Profile CRUD, user search, team membership
- **Team Endpoints**: Team CRUD, member management, invitations
- **Chat Endpoints**: WebSocket message handling, room management
- **Dashboard**: Analytics and summary statistics

## Important Implementation Details

### Database & Entities
- **User Entity**: Comprehensive profile with enums for positions, tech stacks, project goals
- **Team Entity**: Leader/member relationships, project preferences, domain specifications
- **Chat System**: Room management with membership tracking
- **Relationship Mapping**: Proper JPA relationships with cascade rules

### Authentication & Security
- **JWT Configuration**: Custom secret, 1-hour expiration in local profile
- **OAuth2 Integration**: Google-specific configuration in `application-local.yml`
- **CORS Setup**: Environment-specific policies (local vs production)
- **Security Filters**: JWT validation on all protected endpoints

### Development Environment
- **Backend**: H2 console at `/h2-console`, Swagger UI at `/swagger-ui.html`
- **Frontend**: Vite dev server on `:3000`, API defaults to `localhost:8080/api`
- **Docker**: Coordinated services, backend on `:8081`, frontend on `:8080`
- **Testing**: JUnit 5 with H2, Jacoco coverage reports

### Key Configuration Notes
- Frontend API base URL: `VITE_API_URL` environment variable or `localhost:8080/api`
- WebSocket connections require STOMP client setup
- Path alias `@/` points to `front_end/src/` directory
- All tests use H2 database regardless of Spring profile
- Enum-based categorization for type safety and consistency

## Team API Integration (Frontend)

Complete frontend integration for TeamController has been implemented:

### Files Created/Modified:
- `src/types/team.ts` - TypeScript types for all Team entities and DTOs
- `src/api/team.ts` - Complete API functions for all TeamController endpoints
- `src/store/teamStore.ts` - Zustand store for Team state management
- `src/hooks/useTeam.ts` - Custom hook with error handling and toast notifications
- `src/components/features/team/TeamList.tsx` - Example usage component

### API Coverage:
All TeamController endpoints are fully integrated:
- `POST /team` - Create team
- `GET /team` - Get all teams  
- `GET /team/search` - Search teams by criteria
- `GET /team/{teamId}` - Get team details
- `DELETE /team/{teamId}` - Delete team
- `PUT /team` - Update team
- `POST /team/offer` - Submit join/invite requests
- `GET /team/{teamId}/members` - Get team members

### Usage Pattern:
```typescript
import { useTeam } from '@/hooks/useTeam'

const { teams, fetchAllTeams, createTeam, requestJoinTeam } = useTeam()

// Automatic error handling and toast notifications
await fetchAllTeams()
await createTeam({ teamName: "New Team", leaderId: 1 })
```

### State Management:
- Team-specific Zustand store in `teamStore.ts` (separate from matchStore)
- Automatic state synchronization on CRUD operations
- Loading states and error handling built-in

### Type Safety:
- All API functions and state are fully typed
- Union types used instead of enums for compatibility
- Backend DTO structures exactly matched in TypeScript

### JWT Authentication:
Team API requires JWT tokens - ensure axios interceptors are configured for automatic token attachment.

## Complete Team Creation Flow (Updated)

### Backend Implementation (TeamCreateRequest)
완전한 팀 생성 API가 구현되었습니다:

**Files Updated:**
- `TeamCreateRequest.java` - 새로운 팀 생성 요청 DTO with `toTeam()` method
- `TeamController.java` - POST /team endpoint updated to use TeamCreateRequest
- `TeamService.java` - createTeam method updated to use new DTO structure

**Key Features:**
- Role-based member count: backendCount, frontendCount, aiCount, pmCount, designCount
- Backend Enum integration: ProjectGoalEnum, ProjectViveEnum mapped from UI
- Leader assignment: TeamCreateRequest includes leaderId for automatic team leader setup
- Team domain and description support

### Frontend Implementation (make.tsx)
완전한 팀 생성 UI와 API 연동이 완료되었습니다:

**Files Updated:**
- `types/team.ts` - TeamCreateRequest interface added matching backend DTO
- `api/team.ts` - createTeam API updated to use TeamCreateRequest
- `hooks/useTeam.ts` - createTeam hook updated for new request type
- `pages/MakeTeam/make.tsx` - Complete team creation flow with mapping

**Team Creation Features:**
- Domain selection with multiple choice UI
- Project preference selection mapped to ProjectGoalEnum
- Team atmosphere selection mapped to ProjectViveEnum  
- Role distribution with counter controls (backend, frontend, AI, design, PM)
- Team description input with character limit
- Real-time validation and loading states

### UI to Backend Mapping:
```typescript
// Example mapping in make.tsx
const projectPreferenceToEnumMapping: Record<string, ProjectGoalEnum> = {
  '취업우선': 'JOB',
  '수상목표': 'AWARD',
  '포트폴리오중심': 'PORTFOLIO',
  '학습중심': 'STUDY',
  // ... more mappings
}

const atmosphereToEnumMapping: Record<string, ProjectViveEnum> = {
  '반말 지향': 'CASUAL',
  '존대 지향': 'FORMAL',
  '편한 분위기': 'COMFY',
  '체계적 분위기': 'RULE',
  // ... more mappings
}
```

### Complete Team Creation Workflow:
1. User selects domains, project preferences, team atmosphere via UI
2. User sets role distribution (backend: 1, frontend: 1, AI: 0, etc.)
3. User enters team description
4. `mapTeamDataToRequest()` converts UI data to TeamCreateRequest
5. `useTeam.createTeam()` calls backend API
6. Backend creates Team entity with leader assignment
7. Success/failure handling with toast notifications
8. Navigation to home page on success

### Important Notes:
- TypeScript strict mode: Use `import type { ... }` for type-only imports
- Leader ID: Currently hardcoded to 1, should integrate with userStore for actual user
- Error handling: Complete try-catch with user-friendly toast messages
- Validation: Form validation prevents submission with invalid data

### Known Issues Fixed:
- ✅ TypeScript verbatimModuleSyntax errors resolved
- ✅ GoogleSignInButton unused parameter warning fixed  
- ✅ Backend Enum mapping aligned with frontend types
- ✅ Complete API flow from UI to database

## Important Development Notes

### Windows-Specific Configuration
- Docker polling enabled for file change detection: `CHOKIDAR_USEPOLLING=true` and `WATCHPACK_POLLING=true`  
- Vite development server configured with `usePolling: true` for Windows Docker compatibility
- Backend runs on `:8081`, frontend on `:8080` in Docker setup

### Type System & Code Quality  
- TypeScript strict mode enabled with `verbatimModuleSyntax`
- Use `import type { ... }` for type-only imports to avoid compilation errors
- Enum integration: Backend Java enums (ProjectGoalEnum, ProjectViveEnum, etc.) mapped to TypeScript union types
- Zod validation schemas integrated with React Hook Form for form validation

### Testing & Coverage
- Backend: JUnit 5 with Jacoco coverage reports (`./gradlew test jacocoTestReport`)  
- Coverage reports available at `build/reports/jacoco/test/html/`
- All tests use H2 database regardless of Spring profile
- Test isolation ensured with `@Transactional` and cleanup scripts

### Database Schema & Initialization
- Schema definition: `src/main/resources/schema-mysql.sql`
- Test data: `src/main/resources/data.sql` (loaded in local profile)
- H2 console: `http://localhost:8080/h2-console` (local profile only)
- Database URL: `jdbc:h2:~/test` (local) | `jdbc:postgresql://postgres:5432/team_search` (prod)

### API Documentation & Debugging
- Swagger UI: `http://localhost:8080/swagger-ui.html` 
- API Docs: `http://localhost:8080/api-docs`
- Actuator endpoints: `http://localhost:8080/actuator` (health, metrics, prometheus)
- Debug logging enabled for Spring Security OAuth2 in local profile

### Git Workflow & Branch Management
- Main branch: `master` (use for PRs)
- Current feature branch: `feature/S13P11A307-118-api/팀-api-매핑`
- Clean working directory policy - commit frequently with descriptive messages