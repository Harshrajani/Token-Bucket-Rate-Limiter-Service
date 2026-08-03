# API Contract (V1)

## 1. Overview

This document defines the HTTP REST API contract for the Token Bucket Rate Limiter Service.

The API is versioned under:

    /api/v1

The API is divided into two functional areas:

- Administrative APIs — manage rate-limit policies.
- Rate Limiter API — evaluate whether a request should be allowed.

---

# 2. Base URL

    /api/v1

---

# 3. Rate Limiter API

## 3.1 Check Rate Limit

### Endpoint

    POST /api/v1/rate-limit/check

### Purpose

Determine whether a request from a client should be allowed to proceed.

The service identifies the client's rate-limit policy using `clientId`, loads the associated bucket, performs lazy token refill, attempts to consume one token, and returns the rate-limit decision.

---

## Request

### Headers

    Content-Type: application/json

### Body

    {
      "clientId": "user-123"
    }

### Fields

| Field | Type | Required | Description |
|---|---|---|---|
| `clientId` | String | Yes | Unique identifier of the client making the request |

---

# 4. Allowed Response

## HTTP Status

    200 OK

### Response Body

    {
      "status": "ALLOWED",
      "limit": 20,
      "remaining": 14
    }

### Fields

| Field | Type | Description |
|---|---|---|
| `status` | RateLimitStatus | Decision returned by the rate limiter |
| `limit` | Integer | Maximum bucket capacity |
| `remaining` | Integer | Number of tokens remaining after the request |

### Meaning

The request is allowed to proceed.

One token has been consumed from the client's bucket.

---

# 5. Denied Response

## HTTP Status

    429 Too Many Requests

### Response Body

    {
      "status": "DENIED",
      "limit": 20,
      "remaining": 0,
      "retryAfter": 2
    }

### Fields

| Field | Type | Description |
|---|---|---|
| `status` | RateLimitStatus | Decision returned by the rate limiter |
| `limit` | Integer | Maximum bucket capacity |
| `remaining` | Integer | Number of tokens currently available |
| `retryAfter` | Integer | Estimated time before another request may succeed |

### Meaning

The request is rejected because the client currently has no available token.

The client should retry after the specified duration.

---

# 6. Administrative APIs

Administrative APIs are responsible for creating, retrieving, and updating client rate-limit policies.

---

# 7. Create Policy

## Endpoint

    POST /api/v1/admin/policies

## Purpose

Create a new rate-limit policy for a client.

Creating a policy also creates the initial bucket associated with that policy.

---

## Request

### Headers

    Content-Type: application/json

### Body

    {
      "clientId": "user-123",
      "algorithm": "TOKEN_BUCKET",
      "capacity": 20,
      "refillRate": 5
    }

### Fields

| Field | Type | Required | Constraints | Description |
|---|---|---|---|---|
| `clientId` | String | Yes | Must not be blank | Unique client identifier |
| `algorithm` | RateLimitingAlgorithm | Yes | Supported algorithm | Rate-limiting algorithm |
| `capacity` | Integer | Yes | Must be greater than 0 | Maximum number of tokens |
| `refillRate` | Integer | Yes | Must be greater than 0 | Tokens generated per second |

---

## Success Response

### HTTP Status

    201 Created

### Response Body

    {
      "id": 1,
      "clientId": "user-123",
      "algorithm": "TOKEN_BUCKET",
      "capacity": 20,
      "refillRate": 5,
      "createdAt": "2026-08-03T10:15:30Z",
      "updatedAt": "2026-08-03T10:15:30Z"
    }

### Response Headers

The response shall include a `Location` header pointing to the newly created resource.

Example:

    Location: /api/v1/admin/policies/user-123

---

# 8. Get Policy

## Endpoint

    GET /api/v1/admin/policies/{clientId}

## Purpose

Retrieve the rate-limit policy associated with a client.

---

## Path Parameters

| Parameter | Type | Required | Description |
|---|---|---|---|
| `clientId` | String | Yes | Unique client identifier |

---

## Success Response

### HTTP Status

    200 OK

### Response Body

    {
      "id": 1,
      "clientId": "user-123",
      "algorithm": "TOKEN_BUCKET",
      "capacity": 20,
      "refillRate": 5,
      "createdAt": "2026-08-03T10:15:30Z",
      "updatedAt": "2026-08-03T10:15:30Z"
    }

---

# 9. Update Policy

## Endpoint

    PUT /api/v1/admin/policies/{clientId}

## Purpose

Update the rate-limiting configuration for an existing client.

The client identifier is provided through the URL and cannot be changed through the request body.

---

## Path Parameters

| Parameter | Type | Required | Description |
|---|---|---|---|
| `clientId` | String | Yes | Unique client identifier |

---

## Request

### Headers

    Content-Type: application/json

### Body

    {
      "algorithm": "TOKEN_BUCKET",
      "capacity": 50,
      "refillRate": 10
    }

### Fields

| Field | Type | Required | Constraints | Description |
|---|---|---|---|---|
| `algorithm` | RateLimitingAlgorithm | Yes | Supported algorithm | Rate-limiting algorithm |
| `capacity` | Integer | Yes | Must be greater than 0 | Maximum number of tokens |
| `refillRate` | Integer | Yes | Must be greater than 0 | Tokens generated per second |

---

## Success Response

### HTTP Status

    200 OK

### Response Body

    {
      "id": 1,
      "clientId": "user-123",
      "algorithm": "TOKEN_BUCKET",
      "capacity": 50,
      "refillRate": 10,
      "createdAt": "2026-08-03T10:15:30Z",
      "updatedAt": "2026-08-03T10:20:30Z"
    }

---

# 10. Delete Policy

## Endpoint

    DELETE /api/v1/admin/policies/{clientId}

## Purpose

Delete an existing rate-limit policy.

The associated bucket shall also be removed because the bucket has no independent meaning without its policy.

---

## Path Parameters

| Parameter | Type | Required | Description |
|---|---|---|---|
| `clientId` | String | Yes | Unique client identifier |

---

## Success Response

### HTTP Status

    204 No Content

### Response Body

No response body shall be returned.

---

# 11. Rate Limit Status

Rate-limit decisions shall use a strongly typed status enum.

Supported values:

    ALLOWED
    DENIED

The API shall not use arbitrary status strings.

---

# 12. Supported Algorithms

Version 1 supports:

    TOKEN_BUCKET

Future versions may support:

    SLIDING_WINDOW
    LEAKY_BUCKET
    FIXED_WINDOW

The algorithm is selected through the client's rate-limit policy.

---

# 13. Error Response

All application errors shall use a consistent error response structure.

Example:

    {
      "timestamp": "2026-08-03T10:25:30Z",
      "status": 404,
      "error": "Not Found",
      "message": "Policy not found for client: user-123",
      "path": "/api/v1/admin/policies/user-123"
    }

The exact fields may evolve as the global exception handling layer is finalized.

---

# 14. Validation Errors

Invalid request data shall return:

    400 Bad Request

Example:

    {
      "timestamp": "2026-08-03T10:25:30Z",
      "status": 400,
      "error": "Bad Request",
      "message": "Validation failed",
      "path": "/api/v1/admin/policies"
    }

Validation may include:

- Missing `clientId`
- Blank `clientId`
- Missing `algorithm`
- Missing `capacity`
- Capacity less than or equal to zero
- Missing `refillRate`
- Refill rate less than or equal to zero

---

# 15. HTTP Status Codes

| Status | Meaning |
|---:|---|
| `200 OK` | Request successfully processed |
| `201 Created` | New policy successfully created |
| `204 No Content` | Policy successfully deleted |
| `400 Bad Request` | Invalid request or validation failure |
| `404 Not Found` | Policy or bucket does not exist |
| `409 Conflict` | Policy already exists for the client |
| `429 Too Many Requests` | Rate limit exceeded |
| `500 Internal Server Error` | Unexpected server-side failure |

---

# 16. Resource Ownership

The following fields are controlled by the server and must never be accepted from normal client requests:

- `id`
- `createdAt`
- `updatedAt`

The client only provides configuration data.

For policy creation:

    clientId
    algorithm
    capacity
    refillRate

For policy update:

    algorithm
    capacity
    refillRate

---

# 17. Resource Relationships

A client has one rate-limit policy.

A policy has one bucket.

Conceptually:

    Client
       │
       │ 1
       ▼
    RateLimitPolicy
       │
       │ 1
       ▼
    Bucket

The API exposes the policy using `clientId`.

The bucket remains an internal persistence concern and is not directly exposed through the V1 REST API.

---

# 18. Rate Limit Check Flow

The rate-limit check operation follows this logical flow:

    POST /api/v1/rate-limit/check
                │
                ▼
        Validate clientId
                │
                ▼
        Find policy by clientId
                │
                ▼
        Find bucket by policyId
                │
                ▼
        Calculate token refill
                │
                ▼
        Check available tokens
                │
          ┌─────┴─────┐
          │           │
       Token > 0    Token = 0
          │           │
          ▼           ▼
       Consume      Deny
        token         │
          │           │
          ▼           ▼
       Persist      Retry
        bucket       later
          │
          ▼
        ALLOWED

---

# 19. Idempotency

Policy creation is not idempotent.

Repeated requests to:

    POST /api/v1/admin/policies

with the same `clientId` shall result in a conflict after the first successful creation.

The API shall return:

    409 Conflict

for duplicate policy creation.

Policy updates use:

    PUT /api/v1/admin/policies/{clientId}

and are intended to be idempotent.

---

# 20. API Design Principles

The V1 API follows these principles:

- Resource-oriented URLs
- Explicit API versioning
- HTTP semantic status codes
- Strongly typed domain values
- Server-owned identifiers
- Server-owned audit timestamps
- Validation at the API boundary
- Consistent error responses
- Business-oriented resource identification
- Internal persistence details hidden from API consumers

---

# 21. V1 Endpoint Summary

| Method | Endpoint | Purpose | Success |
|---|---|---|---|
| `POST` | `/api/v1/admin/policies` | Create policy | `201 Created` |
| `GET` | `/api/v1/admin/policies/{clientId}` | Get policy | `200 OK` |
| `PUT` | `/api/v1/admin/policies/{clientId}` | Update policy | `200 OK` |
| `DELETE` | `/api/v1/admin/policies/{clientId}` | Delete policy | `204 No Content` |
| `POST` | `/api/v1/rate-limit/check` | Check rate limit | `200 OK` / `429 Too Many Requests` |

---

# 22. Future API Extensions

The following APIs are intentionally outside the initial V1 scope.

Potential future endpoints:

    GET /api/v1/admin/clients/{clientId}/usage

    GET /api/v1/admin/clients/{clientId}/metrics

    GET /api/v1/health

    GET /api/v1/metrics

Future versions may also introduce:

- Authentication
- Authorization
- API keys
- Service-to-service authentication
- gRPC APIs
- Distributed rate limiting
- Administrative dashboard
- Usage analytics
- Prometheus metrics

---

# 23. Contract Evolution

The API contract shall evolve through explicit versioning.

Breaking changes should result in a new API version.

For example:

    /api/v1/...

may eventually become:

    /api/v2/...

Existing V1 clients should not be broken by non-breaking additions.

---

# 24. Current V1 Contract Status

## Implemented

- Create Policy
- Get Policy
- Update Policy
- Rate-limit request DTOs
- Policy request DTOs
- Policy response DTO
- Rate-limit response contract
- Validation contract
- Global error-handling contract

## In Progress

- Token Bucket implementation
- Rate-limit check service
- Rate-limit check controller
- Concurrency handling

## Planned

- Delete Policy API
- Sliding Window
- Authentication
- Metrics
- Distributed rate limiting
- gRPC
