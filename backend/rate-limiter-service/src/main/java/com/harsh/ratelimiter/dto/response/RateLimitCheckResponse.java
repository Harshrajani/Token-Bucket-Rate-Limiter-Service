package com.harsh.ratelimiter.dto.response;

import com.harsh.ratelimiter.constants.enums.RateLimitStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RateLimitCheckResponse {

    private RateLimitStatus status;

    private Integer limit;

    private Integer remaining;

    private Integer retryAfter;

}