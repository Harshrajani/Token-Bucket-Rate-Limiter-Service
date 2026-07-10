package com.harsh.ratelimiter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harsh.ratelimiter.entity.Bucket;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, Long> {

    /**
     * Finds the bucket associated with a policy.
     */
    Optional<Bucket> findByPolicyId(Long policyId);

    /**
     * Checks whether a bucket already exists for a policy.
     */
    boolean existsByPolicyId(Long policyId);

}