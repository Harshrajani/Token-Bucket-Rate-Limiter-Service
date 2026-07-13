package com.harsh.ratelimiter.dto.response;

import java.time.Instant;

import com.harsh.ratelimiter.constants.enums.RateLimitingAlgorithm;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class PolicyResponse {

    private Long id;

    private String clientId;

    private RateLimitingAlgorithm algorithm;

    private Integer capacity;

    private Integer refillRate;

    private Instant createdAt;

    private Instant updatedAt;
}
