package com.harsh.ratelimiter.service.strategy;

import com.harsh.ratelimiter.dto.response.RateLimitCheckResponse;
import com.harsh.ratelimiter.entity.Bucket;
import com.harsh.ratelimiter.entity.RateLimitPolicy;

public interface RateLimitingStrategy {

    RateLimitCheckResponse check(
            RateLimitPolicy policy,
            Bucket bucket
    );

}