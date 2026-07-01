# api-contract.md

# API Contract (V1)

Base URL

/api/v1

---

## Check Rate Limit

Endpoint

POST /check

Purpose

Determine whether a client request should proceed.

### Request

```json
{
  "clientId": "user-123"
}
```

### Success Response — Allowed

HTTP Status:
200

```json
{
  "status": "ALLOW",
  "limit": 100,
  "remaining": 57,
  "resetAfterMs": 200
}
```

Meaning:

* Request accepted
* Remaining capacity available

---

### Success Response — Denied

HTTP Status:
429

```json
{
  "status": "DENY",
  "limit": 100,
  "remaining": 0,
  "retryAfterMs": 1000,
  "reason": "RATE_LIMIT_EXCEEDED"
}
```

Meaning:

* Request rejected
* Retry after specified duration

---

## Configure Client

Endpoint

POST /admin/clients/{clientId}/limits

Purpose

Update rate-limit configuration.

### Request

```json
{
  "algorithm": "TOKEN_BUCKET",
  "requestsPerSecond": 10,
  "burstCapacity": 20
}
```

### Response

```json
{
  "status": "UPDATED"
}
```

---

## Fetch Client Configuration

Endpoint

GET /admin/clients/{clientId}

### Response

```json
{
  "clientId": "user-123",
  "algorithm": "TOKEN_BUCKET",
  "requestsPerSecond": 10,
  "burstCapacity": 20
}
```

---

## Error Responses

400
Invalid request

404
Client not configured

409
Conflict

429
Rate limit exceeded

500
Internal server error

---

# Open Questions

* Should one request consume multiple tokens?
* Should retryAfter represent next token or full refill?
* Should client identifier be extensible?
* Should configuration changes apply immediately?
