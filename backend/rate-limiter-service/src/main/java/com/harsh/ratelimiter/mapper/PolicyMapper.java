package com.harsh.ratelimiter.mapper;

import org.springframework.stereotype.Component;

import com.harsh.ratelimiter.dto.request.CreatePolicyRequest;
import com.harsh.ratelimiter.dto.response.PolicyResponse;
import com.harsh.ratelimiter.entity.RateLimitPolicy;

@Component
public class PolicyMapper {

    public RateLimitPolicy toEntity(CreatePolicyRequest request) {

        RateLimitPolicy entity = new RateLimitPolicy();

        entity.setClientId(request.getClientId());
        entity.setAlgorithm(request.getAlgorithm());
        entity.setCapacity(request.getCapacity());
        entity.setRefillRate(request.getRefillRate());

        return entity;
    }

    public PolicyResponse toResponse(RateLimitPolicy entity) {

        return new PolicyResponse(
                entity.getId(),
                entity.getClientId(),
                entity.getAlgorithm(),
                entity.getCapacity(),
                entity.getRefillRate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}