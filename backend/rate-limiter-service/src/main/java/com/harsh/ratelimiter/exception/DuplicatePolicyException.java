package com.harsh.ratelimiter.exception;

public class DuplicatePolicyException extends RateLimiterException {

    public DuplicatePolicyException(String clientId) {
        super("Rate limit policy already exists for clientId: " + clientId);
    }

}