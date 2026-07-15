package com.harsh.ratelimiter.exception;

public class InvalidRateLimitConfigurationException extends RateLimiterException {

    public InvalidRateLimitConfigurationException(String message) {
        super(message);
    }

}