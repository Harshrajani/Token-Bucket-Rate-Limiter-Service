package com.harsh.ratelimiter.entity;

import com.harsh.ratelimiter.constants.enums.RateLimitingAlgorithm;

import jakarta.persistence.Entity;

import java.time.Instant;
import java.util.UUID;   


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rate_limit_policy")
public class RateLimitPolicy {
    public String id;
    public UUID uuid;
    public String clientId;
    public RateLimitingAlgorithm algorithm;
    public int capacity;
    public int refillRate;
    public Instant createdAt;
    public Instant updatedAt; 
}
