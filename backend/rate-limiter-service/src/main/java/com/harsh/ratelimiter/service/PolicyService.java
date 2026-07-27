package com.harsh.ratelimiter.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harsh.ratelimiter.dto.request.CreatePolicyRequest;
import com.harsh.ratelimiter.dto.request.UpdatePolicyRequest;
import com.harsh.ratelimiter.dto.response.PolicyResponse;
import com.harsh.ratelimiter.entity.Bucket;
import com.harsh.ratelimiter.entity.RateLimitPolicy;
import com.harsh.ratelimiter.exception.BucketNotFoundException;
import com.harsh.ratelimiter.exception.DuplicatePolicyException;
import com.harsh.ratelimiter.exception.PolicyNotFoundException;
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

    @Transactional(readOnly = true)
    public PolicyResponse getPolicy(String clientId) {

        RateLimitPolicy policy = rateLimitPolicyRepository
                .findByClientId(clientId)
                .orElseThrow(() -> new PolicyNotFoundException(clientId));

        return policyMapper.toResponse(policy);
    }

    @Transactional
    public PolicyResponse updatePolicy(
            String clientId,
            UpdatePolicyRequest request) {

        RateLimitPolicy policy = rateLimitPolicyRepository
                .findByClientId(clientId)
                .orElseThrow(() -> new PolicyNotFoundException(clientId));

        policyMapper.updateEntity(request, policy);

        Bucket bucket = bucketRepository
                .findByPolicyId(policy.getId())
                .orElseThrow(() -> new BucketNotFoundException(policy.getId()));

        if (bucket.getAvailableTokens() > policy.getCapacity()) {
            bucket.setAvailableTokens(policy.getCapacity());
        }

        return policyMapper.toResponse(policy);
    }

    @Transactional
public void deletePolicy(String clientId) {

    RateLimitPolicy policy = rateLimitPolicyRepository
            .findByClientId(clientId)
            .orElseThrow(() -> new PolicyNotFoundException(clientId));

    bucketRepository.deleteByPolicyId(policy.getId());

    rateLimitPolicyRepository.delete(policy);
}

}