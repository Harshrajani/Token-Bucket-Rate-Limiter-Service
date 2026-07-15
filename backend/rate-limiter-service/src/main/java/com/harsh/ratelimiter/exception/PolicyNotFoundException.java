package com.harsh.ratelimiter.exception;

public class PolicyNotFoundException extends RateLimiterException {

    public PolicyNotFoundException(String clientId) {
        super("Rate limit policy not found for clientId: " + clientId);
    }

}