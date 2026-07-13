package com.harsh.ratelimiter.dto.request;

import com.harsh.ratelimiter.constants.enums.RateLimitingAlgorithm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePolicyRequest {

    @NotNull
    private RateLimitingAlgorithm algorithm;

    @NotNull
    @Positive
    private Integer capacity;

    @NotNull
    @Positive
    private Integer refillRate;
}