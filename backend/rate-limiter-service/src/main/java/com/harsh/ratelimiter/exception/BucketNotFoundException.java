package com.harsh.ratelimiter.exception;

public class BucketNotFoundException extends RateLimiterException {

    public BucketNotFoundException(Long policyId) {
        super("Bucket not found for policyId: " + policyId);
    }

}