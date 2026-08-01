package com.harsh.ratelimiter.service.strategy;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.harsh.ratelimiter.constants.enums.RateLimitStatus;
import com.harsh.ratelimiter.dto.response.RateLimitCheckResponse;
import com.harsh.ratelimiter.entity.Bucket;
import com.harsh.ratelimiter.entity.RateLimitPolicy;

@Component
public class TokenBucketStrategy implements RateLimitingStrategy {

    @Override
    public RateLimitCheckResponse check(
            RateLimitPolicy policy,
            Bucket bucket) {

        Instant now = Instant.now();

        refill(policy, bucket, now);

        if (bucket.getAvailableTokens() > 0) {

            consumeToken(bucket);

            return new RateLimitCheckResponse(
                    RateLimitStatus.ALLOWED,
                    policy.getCapacity(),
                    bucket.getAvailableTokens(),
                    null
            );
        }

        long retryAfter = calculateRetryAfter(policy);

        return new RateLimitCheckResponse(
                RateLimitStatus.DENIED,
                policy.getCapacity(),
                0,
                retryAfter
        );
    }

    private void refill(
            RateLimitPolicy policy,
            Bucket bucket,
            Instant now) {

        long elapsedSeconds = Duration.between(
                bucket.getLastRefillTime(),
                now
        ).getSeconds();

        if (elapsedSeconds <= 0) {
            return;
        }

        long tokensToAdd =
                elapsedSeconds * policy.getRefillRate();

        long newTokenCount = Math.min(
                (long) bucket.getAvailableTokens() + tokensToAdd,
                policy.getCapacity()
        );

        bucket.setAvailableTokens((int) newTokenCount);
        bucket.setLastRefillTime(now);
    }

    private void consumeToken(Bucket bucket) {

        bucket.setAvailableTokens(
                bucket.getAvailableTokens() - 1
        );
    }

    private long calculateRetryAfter(
            RateLimitPolicy policy) {

        return (long) Math.ceil(
                1.0 / policy.getRefillRate()
        );
    }
}