# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

VietRecruit is a Spring Boot 3.4.2 / Java 21 Applicant Tracking System (ATS). It handles job postings, candidate applications, interviews, scorecards, and offers for recruitment workflows.

## Technology Stack & Tools

-   **Language**: Java 21
-   **Framework**: Spring Boot 3.4.2
-   **Database**: PostgreSQL 16 equipped with `pgvector` for AI similarity calculations.
-   **Schema Migrations**: Flyway.
-   **Caching / Session**: Redis.
-   **Messaging / Events**: Apache Kafka (async operations such as triggering emails and webhooks).
-   **Object Mapping**: MapStruct & Lombok.
-   **Resilience**: Resilience4j.
-   **Security**: Spring Security 6 utilizing stateless JWTs, mapped to OAuth2 identity providers (Google, Github).
-   **Payment Gateway**: PayOS native API integration with webhook signature verification.
-   **Build Tool**: Maven (`pom.xml`).
-   **CI/CD**: `Jenkinsfile` combined with GitHub Actions (`.github/workflows`) for robust automated rollouts.
-   **Testing**: Testcontainers alongside JUnit/Mockito for rigorous integration scenarios.
-   **API Documentation**: Provided via Swagger/OpenAPI (locally) and documented Postman collections (`postman/`).

## Common Commands

```bash
# Start all infrastructure + run app
make run

# Stop Docker infrastructure
make stop

# Build (skip tests)
./mvnw clean package -DskipTests

# Run all unit tests (excludes integration test)
./mvnw test -Dtest='!com.vietrecruit.ApplicationTests'

# Run a single test class
./mvnw test -Dtest=com.vietrecruit.feature.user.service.impl.ClientUserServiceImplTest

# Run integration test (requires Docker for TestContainers)
./mvnw test -Dtest=com.vietrecruit.ApplicationTests

# Check code formatting
./mvnw spotless:check

# Auto-fix code formatting
./mvnw spotless:apply
```

## Code Formatting

Spotless with **Google Java Format (AOSP style)** is enforced. Rules:
- 4-space tabs
- Import order: `java`, `jakarta`, `org`, `com`
- Unused imports removed automatically
- Trailing whitespace trimmed, final newline enforced

Run `./mvnw spotless:apply` before committing. Add `-Dspotless.check.skip=true` to skip during builds if needed.

## Architecture

### Module Layout

```text
VietRecruit/
├── .agent/                     # AI Agent configuration, skills, workflows, and rules
├── .github/                    # GitHub Actions workflows for CI/CD
├── assets/                     # Static project assets (images, system diagrams)
├── database/                   # Database scripts and aggregate Flyway init scripts
├── debezium/                   # Debezium configurations for CDC (Change Data Capture)
├── docs/                       # Project Documentation (Architecture, APIs, rules)
├── elk/                        # ELK Stack configuration (Elasticsearch, Logstash, Kibana)
├── grafana-storage/            # Persistent storage volume for Grafana
├── infra/                      # Legacy/alternative infrastructure configs
├── monitor/                    # Prometheus monitoring configurations
├── postman/                    # API Collections (Postman) for testing
├── scripts/                    # Utility scripts for development and deployment
├── src/                        # Main Application Source Code (Java/Spring Boot)
│   ├── main/
│   │   ├── java/com/vietrecruit/
│   │   │   ├── common/         # Shared horizontal utilities and base classes
│   │   │   │   ├── base/       # Abstract Controller and Entity enforcing common behaviors
│   │   │   │   ├── config/     # Global configurations (Web, Security, Kafka, Redis, OpenAPI)
│   │   │   │   ├── enums/      # Global enums (ApiErrorCode, ApiSuccessCode, UserRole)
│   │   │   │   ├── exception/  # Global Exception Handler returning standard API responses
│   │   │   │   ├── init/       # Application seeders (e.g. creating default admin)
│   │   │   │   ├── response/   # Standardized wrapper classes (ApiResponse, PageResponse)
│   │   │   │   ├── security/   # JWT utilities, Auth Managers, OAuth2 flows
│   │   │   │   ├── storage/    # Multi-provider storage framework (e.g. Cloudflare R2)
│   │   │   │   └── util/       # Application-wide utility helper classes
│   │   │   ├── feature/        # Vertical feature modules (Domain-Driven Design)
│   │   │   │   ├── application/# ATS: Applications, Interviews, Scorecards, Offers
│   │   │   │   ├── auth/       # Authentication (Login, Register, Password Reset, Refresh)
│   │   │   │   ├── candidate/  # Candidate profiles and resume management
│   │   │   │   ├── category/   # Job categories reference system
│   │   │   │   ├── company/    # Employer company profiles and branding
│   │   │   │   ├── department/ # Internal employer departments
│   │   │   │   ├── job/        # Job postings, visibility controls, and search attributes
│   │   │   │   ├── location/   # Job locations reference system
│   │   │   │   ├── notification/# Asynchronous email & in-app alerts (via Kafka)
│   │   │   │   ├── payment/    # PayOS integration and webhook verification
│   │   │   │   ├── subscription/# Employer subscription plans, billing, and posting quotas
│   │   │   │   └── user/       # General user account records and management
│   │   │   └── Application.java# Spring Boot application entry point
│   │   └── resources/
│   │       ├── db/migration/   # Flyway SQL migrations determining database schema
│   │       ├── resilience/     # Resilience4j YAML files for Circuit Breaker/Rate Limiting
│   │       ├── templates/email # Thymeleaf/Freemarker HTML email templates
│   │       ├── application.yaml# Core application configuration
│   │       ├── application.dev.yaml  # Dev profile (local backing services)
│   │       ├── application.prod.yaml # Prod profile (managed remote backing services)
│   │       └── logback-spring.xml    # Advanced ELK / Async Logging configuration
│   └── test/                   # Unit tests and Testcontainers integration suites
├── .env                        # Local development environment variables
├── .env.example                # Template for environment variables
├── .env.prod                   # Production environment variables
├── docker-compose.yml          # Local infrastructure (Postgres, Redis, Kafka, Zookeeper)
├── docker-compose.prod.yml     # Production infrastructure including observability stack
├── Dockerfile                  # Production-ready multi-stage Docker build
├── Jenkinsfile                 # Jenkins CI/CD declarative pipeline
├── Makefile                    # Make command shortcuts for developers
└── pom.xml                     # Maven definitions and dependency lock
```

### Feature Modules

Each feature module follows the pattern: `controller → service interface → service impl → repository → entity`. DTOs are mapped with **MapStruct**.

| Module | Responsibility |
|--------|----------------|
| `auth` | JWT (RS256), OAuth2 (Google/GitHub), email verification, refresh tokens |
| `user` | Admin and client user management, RBAC |
| `company` | Company/organization profiles |
| `department` | Department structures within companies |
| `job` | Job postings, quotas, status lifecycle |
| `candidate` | Candidate profiles |
| `application` | Application tracking, status history |
| `interview` | Interview scheduling with strict status transitions |
| `scorecard` | Interview evaluations with uniqueness constraints |
| `payment` | PayOS payment transactions and webhooks |
| `subscription` | Employer subscription plans and billing |
| `notification` | Email notifications via Resend API |
| `category`, `location` | Reference data |

### Source Code Architecture (`src/main/java`)

The project follows a **Modular Monolith** approach with a **Layered Architecture** inside each vertical feature module.

#### A. The `common` Module
This acts as the cross-cutting concern layer used by all features.
- `base`: Contains abstract `BaseController` and `BaseEntity` ensuring uniform audit fields (createdAt, updatedAt) and resilience handling.
- `config`: Global configurations for Spring Security, Kafka, Redis, and Swagger/OpenAPI.
- `exception`: Contains `GlobalExceptionHandler` to translate all exceptions into a standard `ApiException` and uniform `ApiResponse`.
- `init`: Application initializers (e.g., seeding admin users on first startup).
- `response`: Standardized API wrappers (`ApiResponse`, `PageResponse`) to ensure all endpoints respond consistently.
- `security`: JWT filters, Authentication managers, and OAuth2 setups (Google, GitHub).

#### B. The `feature` Modules (Domain-Driven Design)
The domain is split into strict business capabilities containing distinct layers. Features represent isolated system components with limited cross-coupling.
Inside each module (e.g., `job`, `subscription`, `payment`):
- `controller/`: REST API endpoints. Extends `BaseController` for common resilience patterns and standardized error handling.
- `service/`: Interfaces and `impl/` implementations for pure business logic and transaction boundaries.
- `repository/`: Spring Data JPA interfaces. Includes `Specification` files targeting specific database-level search capabilities.
- `entity/`: JPA annotated domain models mirroring the relational schema.
- `mapper/`: MapStruct interfaces used to cleanly uncouple Entities from external representation DTOs.
- `dto/`: Immutable Request/Response objects isolated to controller interaction payload contracts.

### Database

Flyway manages 25 migrations (`src/main/resources/db/migration/`). PostgreSQL custom enum types are defined in V1 and used throughout:

- `application_status`: `NEW → SCREENING → INTERVIEW → OFFER → HIRED | REJECTED`
- `interview_status`: `SCHEDULED → COMPLETED | CANCELED`
- `job_status`: `DRAFT → PUBLISHED → CLOSED`
- `offer_status`, `scorecard_result`, `subscription_status`, `payment_status`, `billing_cycle`

The `pgvector` extension supports AI embeddings. WAL is set to `logical` for Debezium CDC.

### Security

- JWT access + refresh tokens with RS256 signatures
- OAuth2 social login (Google, GitHub)
- Email verification flow
- RBAC via Spring Security

### Infrastructure Composition

Docker Compose is split across:
- `infra/database/` — PostgreSQL, Redis
- `infra/application/` — Kafka, Debezium, Elasticsearch, Logstash, Kibana
- `infra/monitoring/` — Prometheus, Grafana (optional, commented out by default)

The root `docker-compose.yml` includes these via `include:`.

## Environment Setup

Copy `.env.example` to `.env`. Required variables include: `POSTGRES_URL`, `POSTGRES_USER`, `POSTGRES_PASSWORD`, `REDIS_HOST`, `REDIS_PORT`, `REDIS_PASSWORD`, `KAFKA_BOOTSTRAP_SERVERS`, `JWT_SECRET`, `OPENAI_API_KEY`, `PAYOS_CLIENT_ID`, `PAYOS_API_KEY`, `R2_ENDPOINT`, `R2_BUCKET`, `R2_ACCESS_KEY`, `R2_SECRET_KEY`, `RESEND_API_KEY`, `GOOGLE_CLIENT_ID`, `GITHUB_CLIENT_ID`.

## CI/CD

- **GitHub Actions** (`.github/workflows/test.yml`): runs unit tests on push to `main`/`release`, caches Maven deps, skips integration tests.
- **Jenkins** (`Jenkinsfile`): builds Docker image, pushes to Docker Hub, deploys to VPS.
