# DECISIONS.md

# Architecture Decision Log

This document records the major architectural and design decisions made during the development of the Rate Limiter Service.

Each decision includes:

- Context
- Decision
- Alternatives Considered
- Reasoning
- Future Considerations

---

# ADR-001

## Build a Standalone Rate Limiter Service

### Status

Accepted

### Context

Most applications use in-process rate limiting libraries.

While simple, they tightly couple rate limiting logic with the application.

### Decision

Build rate limiting as an independent backend service.

Applications call this service before processing requests.

### Why?

Advantages

- Centralized configuration
- Reusable by multiple services
- Independent deployment
- Easier monitoring
- Easier future scaling

### Alternatives

- Embedded library
- API Gateway only
- Reverse proxy rate limiting

### Future

Service will later support gRPC and distributed deployments.

---

# ADR-002

## Use PostgreSQL for Persistence

### Status

Accepted

### Context

Bucket state must survive application restarts.

### Decision

Persist both Policy and Bucket in PostgreSQL.

### Why?

Advantages

- ACID transactions
- Strong consistency
- Reliable persistence
- Easy local development
- Excellent Spring Boot integration

### Alternatives

- Redis
- MongoDB
- In-memory HashMap

### Future

Redis may become the primary storage for distributed mode while PostgreSQL remains the source of truth.

---

# ADR-003

## Separate Policy from Bucket

### Status

Accepted

### Context

Configuration changes infrequently.

Runtime state changes on every request.

### Decision

Create two separate entities.

RateLimitPolicy

- configuration

Bucket

- runtime state

### Why?

Advantages

- Clear separation of concerns
- Independent lifecycle
- Smaller updates
- Easier maintenance

### Alternatives

Store everything in a single table.

Rejected because runtime writes would constantly update configuration rows.

---

# ADR-004

## Bucket References Policy Using policyId

### Status

Accepted

### Context

JPA allows entity relationships.

### Decision

Bucket stores

policyId

instead of

@OneToOne RateLimitPolicy

### Why?

Advantages

- Simpler entities
- Explicit database access
- Less Hibernate complexity
- Easier migration to microservices

### Alternatives

@OneToOne relationship

Rejected for V1.

May be reconsidered if future requirements demand object navigation.

---

# ADR-005

## Client Lookup Uses clientId

### Status

Accepted

### Context

Incoming requests only know the client identifier.

### Decision

Repository lookup

findByClientId()

instead of

findById()

### Why?

Business APIs should expose business identifiers.

Database IDs are implementation details.

### Alternatives

Lookup by database ID.

Rejected.

---

# ADR-006

## Use Enum for Algorithm

### Status

Accepted

### Decision

Algorithms are represented using

RateLimitingAlgorithm

### Why?

Advantages

- Type safety
- Compile-time validation
- Easier Strategy Pattern integration
- No invalid string values

### Alternatives

Store String values.

Rejected.

---

# ADR-007

## Lazy Bucket Refill

### Status

Accepted

### Context

Token Bucket requires periodic token generation.

### Decision

Refill only when a request arrives.

### Why?

Advantages

- No scheduler
- O(1) computation
- Minimal database writes
- Excellent scalability

Formula

elapsed time

↓

tokens to add

↓

clamp to capacity

### Alternatives

Background scheduler

Rejected due to unnecessary work.

---

# ADR-008

## Use Integer for Tokens

### Status

Accepted

### Decision

availableTokens

capacity

refillRate

are Integer.

### Why?

Advantages

- Whole-number semantics
- Smaller storage
- Better readability
- Simpler calculations

### Alternatives

Long

Double

BigDecimal

Rejected for V1.

---

# ADR-009

## REST First, gRPC Later

### Status

Accepted

### Context

Need fast development and easier debugging.

### Decision

Version 1 uses REST.

Version 2 introduces gRPC.

### Why?

REST

Advantages

- Easy testing
- Human readable
- Postman support
- Browser support

gRPC

Advantages

- Faster
- Binary protocol
- Streaming
- Better internal service communication

---

# ADR-010

## Repository Interfaces

### Status

Accepted

### Decision

Repositories are interfaces extending

JpaRepository

### Why?

Spring generates implementations automatically.

Advantages

- Less boilerplate
- Built-in CRUD
- Query derivation
- Easier maintenance

### Alternatives

Manual repository implementations.

Rejected.

---

# ADR-011

## No Service Interfaces in V1

### Status

Accepted

### Context

Only one implementation exists.

### Decision

Services are concrete classes.

### Why?

Avoid unnecessary abstraction.

Interfaces should represent multiple implementations.

### Future

Introduce interfaces only when multiple implementations become necessary.

---

# ADR-012

## Strategy Pattern Deferred to V2

### Status

Accepted

### Context

Only Token Bucket exists in V1.

### Decision

Implement Token Bucket directly.

Introduce Strategy Pattern when multiple algorithms exist.

### Why?

Avoid premature abstraction.

### Future

RateLimitingStrategy

↓

TokenBucketStrategy

SlidingWindowStrategy

LeakyBucketStrategy

---

# ADR-013

## PostgreSQL Generates Primary Keys

### Status

Accepted

### Decision

Use

GenerationType.IDENTITY

### Why?

Advantages

- Simplicity
- Database-managed IDs
- Suitable for learning project

### Future

Evaluate SEQUENCE for higher throughput.

---

# ADR-014

## Server Owns Audit Fields

### Status

Accepted

### Decision

createdAt

updatedAt

are managed by the server.

### Why?

Clients should never control auditing information.

Implementation uses

@PrePersist

@PreUpdate

---

# ADR-015

## Bucket Lifetime Depends on Policy

### Status

Accepted

### Decision

Every Bucket belongs to exactly one Policy.

Deleting a Policy should remove its Bucket.

### Why?

Bucket has no meaning without configuration.

---

# ADR-016

## Initial Concurrency Strategy

### Status

Planned

### Context

Multiple concurrent requests may consume the same token.

### Decision

Concurrency handling will be implemented after core functionality.

### Candidates

- Optimistic Locking
- Pessimistic Locking
- Redis Distributed Lock

Decision deferred until V1 is complete.

---

# ADR-017

## API Response Format

### Status

Accepted

Allowed Response

```json
{
  "status": "ALLOWED",
  "limit": 20,
  "remaining": 15
}
```

Denied Response

```json
{
  "status": "DENIED",
  "limit": 20,
  "remaining": 0,
  "retryAfter": 2
}
```

### Why?

Simple

Predictable

Easy for clients to consume.

---

# ADR-018

## Package Organization

### Status

Accepted

```
controller/

service/

repository/

entity/

dto/

exception/

constants/

config/
```

### Why?

Layered architecture is familiar, maintainable and appropriate for V1.

Future versions may evolve toward Clean Architecture.

---

# ADR-019

## Time Representation

### Status

Accepted

### Decision

Use

Instant

instead of

Date

LocalDateTime

Timestamp

### Why?

Advantages

- Time-zone independent
- UTC by default
- Modern Java API
- Recommended for backend services

---

# Future ADRs

The following decisions are intentionally postponed until they become necessary.

- Clean Architecture
- CQRS
- Event Sourcing
- Redis Distributed Cache
- Distributed Locking
- Optimistic Locking
- Prometheus Metrics
- Grafana Dashboard
- Kubernetes Deployment
- gRPC
- API Versioning
- Multi-region deployment
- Authentication & Authorization
- API Gateway integration

These decisions will be documented when they are introduced rather than anticipated prematurely.

---

# Philosophy

Every architectural decision should answer one question:

> **Why was this choice made?**

Code explains *how* the system works.

Architecture explains *why* the system was designed that way.

This document is expected to evolve throughout the lifetime of the project.