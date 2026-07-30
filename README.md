# Token Bucket Rate Limiter Service

A standalone, configurable rate-limiting service built with Java and Spring Boot.

The service provides a centralized API for determining whether requests from individual clients should be allowed or denied based on configurable rate-limiting policies.

The project focuses on building a production-oriented backend service with persistent state, configurable policies, concurrency safety, and an extensible architecture.

---

## ✨ Features

- Per-client rate-limiting policies
- Token Bucket rate-limiting algorithm
- Configurable bucket capacity and refill rate
- Persistent rate-limit state
- Policy management APIs
- REST API
- Request validation
- Global exception handling
- Transactional operations
- Concurrency-safe request processing
- Extensible algorithm architecture
- Automated testing
- Load testing

---

## 🏗️ Tech Stack

| Category | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| Web | Spring Web MVC |
| Persistence | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| Build Tool | Maven |
| Containerization | Docker / Docker Compose |
| Testing | JUnit / Spring Boot Test |
| Database Management | pgAdmin |

---

## 📌 API Overview

### Policy Management

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/admin/policies` | Create a rate-limit policy |
| `GET` | `/api/v1/admin/policies/{clientId}` | Get a policy |
| `PUT` | `/api/v1/admin/policies/{clientId}` | Update a policy |
| `DELETE` | `/api/v1/admin/policies/{clientId}` | Delete a policy |

### Rate Limiting

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/rate-limit/check` | Check whether a request should be allowed |

### Example Request

    {
      "clientId": "payment-service"
    }

### Example Response

    {
      "status": "ALLOWED",
      "limit": 20,
      "remaining": 19
    }

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven
- Docker
- Docker Compose

### 1. Clone the Repository

    git clone <repository-url>
    cd rate-limiter-service

### 2. Start PostgreSQL

    docker compose up -d

### 3. Run the Application

    ./mvnw spring-boot:run

The service will be available at:

    http://localhost:8080

---

## 🗄️ Database

The application uses PostgreSQL for persistent storage.

Docker Compose provides:

- PostgreSQL
- pgAdmin

### Default Development Database

    Database: rate_limiter_db
    Host: localhost
    Port: 5432

### pgAdmin

    http://localhost:5050

> Development credentials are configured in `compose.yml`. Production deployments should use environment variables or a secrets manager.

---

## 📖 Documentation

Detailed project documentation is maintained separately:

- [ARCHITECTURE.md](ARCHITECTURE.md) — System architecture and component responsibilities
- [REQUIREMENTS.md](REQUIREMENTS.md) — Functional and non-functional requirements
- [API-CONTRACT.md](API-CONTRACT.md) — API specifications and contracts
- [DECISIONS.md](DECISIONS.md) — Architectural decisions and their rationale

---

## 📊 Project Status

### Completed

- [x] Project setup
- [x] PostgreSQL integration
- [x] Docker Compose setup
- [x] Domain entities
- [x] Repository layer
- [x] DTOs and mappers
- [x] Exception handling
- [x] Policy management APIs

### In Progress

- [ ] Token Bucket implementation
- [ ] Rate-limit check API
- [ ] Concurrency handling
- [ ] Integration testing
- [ ] Load testing

---

## 🛣️ Roadmap

### V1 — Core Rate Limiter

- Token Bucket
- Persistent bucket state
- Per-client configuration
- Concurrency-safe processing
- REST API
- Automated testing
- Load testing

### V2 — Extensibility & Performance

- Sliding Window
- Additional rate-limiting strategies
- gRPC
- Performance improvements

### V3 — Distributed Rate Limiting

- Redis
- Distributed state
- Horizontal scaling
- Distributed concurrency control

### V4 — Observability

- Metrics
- Prometheus
- Grafana
- Request and rejection monitoring

---

## 🎯 Project Goals

This project is being developed to explore practical backend engineering concepts through a real-world distributed-system problem.

Key areas include:

- API design
- Persistence
- Concurrency
- Time-based algorithms
- Transaction management
- Extensible architecture
- Performance testing
- Distributed systems

---

## 👨‍💻 Development Approach

The project is intentionally developed incrementally.

Each feature is introduced with a specific engineering requirement rather than adding infrastructure prematurely.

Architectural decisions and their reasoning are documented separately in `DECISIONS.md`.

---

## 📄 License

This project is currently intended for educational and portfolio purposes.
