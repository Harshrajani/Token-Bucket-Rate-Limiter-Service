package com.harsh.ratelimiter.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harsh.ratelimiter.dto.request.CreatePolicyRequest;
import com.harsh.ratelimiter.dto.response.PolicyResponse;
import com.harsh.ratelimiter.entity.Bucket;
import com.harsh.ratelimiter.entity.RateLimitPolicy;
import com.harsh.ratelimiter.exception.DuplicatePolicyException;
import com.harsh.ratelimiter.mapper.PolicyMapper;
import com.harsh.ratelimiter.repository.BucketRepository;
import com.harsh.ratelimiter.repository.RateLimitPolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final RateLimitPolicyRepository rateLimitPolicyRepository;

    private final BucketRepository bucketRepository;

    private final PolicyMapper policyMapper;

    @Transactional
    public PolicyResponse createPolicy(CreatePolicyRequest request) {

        if (rateLimitPolicyRepository.existsByClientId(request.getClientId())) {
            throw new DuplicatePolicyException(request.getClientId());
        }

        RateLimitPolicy policy = policyMapper.toEntity(request);

        RateLimitPolicy savedPolicy = rateLimitPolicyRepository.save(policy);

        Bucket bucket = new Bucket();
        bucket.setPolicyId(savedPolicy.getId());
        bucket.setAvailableTokens(savedPolicy.getCapacity());
        bucket.setLastRefillTime(savedPolicy.getCreatedAt());

        bucketRepository.save(bucket);

        return policyMapper.toResponse(savedPolicy);
    }

}