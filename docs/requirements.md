# requirements.md

# Functional Requirements

## FR-01 — Rate Decision Endpoint

Expose an endpoint that accepts a client identifier and returns a rate-limit decision.

Decision:

* ALLOW
* DENY

---

## FR-02 — Client Configuration

Support per-client configurable limits.

Configuration:

* Requests per second
* Bucket capacity (burst size)
* Algorithm type

---

## FR-03 — Persistent State

Rate-limit decisions should remain consistent after service restart.

---

## FR-04 — Concurrency Safety

Concurrent requests for the same client must not allow double consumption of tokens.

---

## FR-05 — Multiple Algorithms

Support:

* Token Bucket (V1)
* Sliding Window (Future)

Algorithm should be selectable per client.

---

## FR-06 — Response Metadata

Every decision should expose rate-limit information.

Examples:

* Current limit
* Remaining capacity
* Retry time

---

## FR-07 — Load Validation

Validate correctness under high concurrency.

Initial target:

* 500+ requests per second

---

# Non Functional Requirements

## Performance

* Low response latency
* Stable under load

## Reliability

* Consistent decisions

## Availability

* Service remains operational during normal traffic spikes

## Scalability

* Architecture should support future distributed expansion

---

# Constraints

V1:

* Single service instance
* REST communication
* No dashboard
* No distributed state

---

# Stretch Goals

* Distributed limiter mode
* Dashboard
* Metrics and monitoring
* gRPC support
