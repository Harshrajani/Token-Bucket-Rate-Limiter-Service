package com.harsh.ratelimiter.dto.request;

import com.harsh.ratelimiter.constants.enums.RateLimitingAlgorithm;

public class CreatePolicyRequest {
    public String clientId;
    public RateLimitingAlgorithm algorithm;
    public Integer capacity;
    public Integer refillRate;
}
