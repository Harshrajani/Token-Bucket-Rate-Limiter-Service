package com.harsh.ratelimiter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harsh.ratelimiter.entity.RateLimitPolicy;

@Repository
public interface RateLimitPolicyRepository extends JpaRepository<RateLimitPolicy, Long> {

    /**
     * Finds a rate limiting policy by client identifier.
     */
    Optional<RateLimitPolicy> findByClientId(String clientId);

    /**
     * Checks whether a policy already exists for the given client.
     */
    boolean existsByClientId(String clientId);

}