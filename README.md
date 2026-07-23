8# README.md

# Token Bucket Rate Limiter Service

## Overview

Token Bucket Rate Limiter Service is a backend system responsible for controlling how frequently a client can send requests to protected services.

The service exposes an API that receives a client identifier and returns whether the request should be allowed or denied according to configured rate-limiting rules.

This project is built to learn backend engineering fundamentals through real-world concerns such as API design, concurrency, persistence, configuration management, observability, and system evolution.

---

## Problem Statement

Backend systems can become overloaded when clients send requests too quickly or in bursts.

This service protects backend APIs from excessive or abusive traffic by enforcing configurable request limits while maintaining fairness and availability.

---

## Why This Project

Many applications use libraries for rate limiting, but building rate limiting as a standalone service introduces important backend concepts:

* API contract design
* Shared mutable state
* Concurrency control
* Time-based systems
* Persistence
* Horizontal scalability
* System evolution

---

## Users

### API Consumers

Applications or users sending requests.

Examples:

* Mobile apps
* Frontend applications
* Partner APIs

### Protected Services

Services that integrate with the rate limiter.

Examples:

* Auth Service
* Payment Service
* Order Service

### Administrators

Configure and manage rate limits.

---

## Version Roadmap

### V1

* REST API
* Token Bucket algorithm
* Per-client configuration
* Persistent bucket state
* Concurrency-safe updates

### V2

* gRPC interface
* Improved performance7
* Internal service-to-service communication

### V3

* Distributed deployment
* Shared state

### V4

* Dashboard and observability

---

## Success Criteria

* Correct ALLOW / DENY decisions
* Concurrent requests remain safe
* Limits survive restart
* Low response latency
* Configurations apply correctly

---

## Non Goals (V1)

* Authentication system
* Billing
* Analytics platform
* Geo-based limits
* Distributed deployment
* Dashboard UI

---

## Tech Stack (Planned)

Backend:

* Java
* Spring Boot

Storage:

* Postgres Sql

Testing:

* JUnit Testing

Deployment:

* TBD

---

## Project Status

Planning Phase
