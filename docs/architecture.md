# Architecture.md

# Token Bucket Rate Limiter Service

## Overview

The Token Bucket Rate Limiter Service is a standalone backend service responsible for deciding whether an incoming request from a client should be **ALLOWED** or **DENIED** based on configurable rate limiting policies.

Unlike application-level libraries, this service is designed to be shared by multiple applications over HTTP (REST in V1, gRPC in V2).

---

# High-Level Architecture

                    Client Application
                            │
                            │
                            ▼
                Rate Limiter REST API
                            │
                            ▼
                 RateLimiterService
                            │
        ┌───────────────────┼────────────────────┐
        │                   │                    │
        ▼                   ▼                    ▼
 Policy Repository     Bucket Repository     Strategy (V2)
        │                   │                    │
        └──────────────┬────┘                    │
                       ▼                         │
                   PostgreSQL                   │
                                                 ▼
                               Token Bucket / Sliding Window

---

# Core Concepts

## Client

A client represents any consumer of the protected APIs.

Examples:

- User
- API Key
- Service
- Application

Each client owns exactly one rate limiting policy.

---

## Rate Limit Policy

A policy defines **how** a client should be rate limited.

Example:

Client

```
clientId = payment-service
```

Policy

```
capacity = 20
refillRate = 5 tokens/sec
algorithm = TOKEN_BUCKET
```

The policy contains only configuration.

It never stores runtime information.

---

## Bucket

The bucket represents runtime state.

Example

```
availableTokens = 13
lastRefillTime = 2026-07-11T14:00:00Z
```

The bucket changes on every request.

---

# Data Model

RateLimitPolicy

```
id
clientId
algorithm
capacity
refillRate
createdAt
updatedAt
```

Bucket

```
id
policyId
availableTokens
lastRefillTime
```

Relationship

```
RateLimitPolicy

1

↓

1

Bucket
```

---

# Request Flow

Incoming Request

```
POST /api/v1/rate-limit/check
```

Execution Flow

```
Receive Request

↓

Validate Request

↓

Load Policy using clientId

↓

Load Bucket using policyId

↓

Refill Bucket (Lazy)

↓

Consume Token

↓

Persist Bucket

↓

Return Response
```

---

# Why Lazy Refill?

The service does not use a scheduler.

Instead,

tokens are calculated only when a request arrives.

Benefits

- No background jobs
- No unnecessary database writes
- O(1) refill calculation
- Scales to millions of clients

Formula

```
elapsedTime

↓

tokensToAdd

↓

currentTokens + tokensToAdd

↓

min(capacity)
```

---

# Package Structure

```
com.harsh.ratelimiter

├── config
├── constants
│     └── enums
├── controller
├── dto
│     ├── request
│     └── response
├── entity
├── exception
├── repository
├── service
├── strategy (V2)
├── util
└── RateLimiterApplication
```

---

# Responsibilities

## Controller

Responsible only for

- HTTP requests
- Validation
- Returning HTTP responses

No business logic.

---

## Service

Responsible for

- Business workflow
- Policy validation
- Bucket processing
- Response generation

---

## Repository

Responsible only for persistence.

No business logic.

---

## Entity

Represents database models.

Entities should not contain HTTP-specific code.

---

# Persistence Layer

Database

```
PostgreSQL
```

ORM

```
Spring Data JPA
Hibernate
```

Repositories

```
RateLimitPolicyRepository

BucketRepository
```

---

# Rate Limiting Algorithms

## V1

✔ Token Bucket

---

## V2

✔ Token Bucket

✔ Sliding Window

Implemented using Strategy Pattern.

```
RateLimitingStrategy

        ▲

        │

──────────────────────────────

│                           │

TokenBucketStrategy   SlidingWindowStrategy
```

---

# Concurrency

Multiple requests may arrive simultaneously for the same client.

Goals

- No double spending of tokens
- Atomic bucket updates
- Consistent remaining token count

Implementation will be added after V1.

Possible approaches

- Optimistic Locking
- Pessimistic Locking
- Distributed Locking (Redis)

---

# API Endpoints

## Admin

Create Policy

```
POST /api/v1/admin/policies
```

Update Policy

```
PUT /api/v1/admin/policies/{clientId}
```

Get Policy

```
GET /api/v1/admin/policies/{clientId}
```

---

## Rate Limiter

Check Request

```
POST /api/v1/rate-limit/check
```

---

# Response Format

Allowed

```json
{
  "status": "ALLOWED",
  "limit": 20,
  "remaining": 15
}
```

Denied

```json
{
  "status": "DENIED",
  "limit": 20,
  "remaining": 0,
  "retryAfter": 2
}
```

---

# Design Principles

- Single Responsibility Principle
- Separation of Concerns
- Business-first Repository Design
- Lazy Refill
- Strategy Pattern (V2)
- REST First, gRPC Later
- Explicit Database Access
- Clean Package Structure
- Immutable API Contracts

---

# Current Progress

## Completed

- Project Setup
- Docker Compose
- PostgreSQL
- Spring Boot Configuration
- RateLimitPolicy Entity
- Bucket Entity
- Repository Layer
- Architecture Design

---

## Next Milestones

- DTO Design
- Exception Handling
- Admin APIs
- Token Bucket Algorithm
- Rate Limit Check API
- Concurrency Handling
- Sliding Window Strategy
- Integration Testing
- Load Testing
- gRPC Support
- Distributed Mode

---

# Future Roadmap

V1

- REST APIs
- Token Bucket
- PostgreSQL
- Single Instance

V2

- Strategy Pattern
- Sliding Window
- gRPC
- Redis
- Dashboard

V3

- Distributed Rate Limiter
- Multiple Service Instances
- Metrics
- Prometheus
- Grafana
- Kubernetes Deployment

---

# Architecture Philosophy

This project is intentionally designed as a standalone rate limiting service instead of embedding a library inside applications.

The objective is to simulate a production-grade backend service where multiple applications rely on a centralized decision engine for request throttling.

The architecture prioritizes

- Scalability
- Extensibility
- Maintainability
- Clear separation of responsibilities
- Production-ready design