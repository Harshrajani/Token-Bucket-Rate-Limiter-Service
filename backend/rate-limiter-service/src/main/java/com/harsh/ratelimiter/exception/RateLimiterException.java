package com.harsh.ratelimiter.exception;

public abstract class RateLimiterException extends RuntimeException {

    public RateLimiterException(String message) {
        super(message);
    }

    public RateLimiterException(String message, Throwable cause) {
        super(message, cause);
    }
}