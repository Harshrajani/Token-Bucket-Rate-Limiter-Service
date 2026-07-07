package com.harsh.ratelimiter.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bucket")
public class Bucket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "policy_id", nullable = false, unique = true)
    private Long policyId;

    @NotNull
    @PositiveOrZero
    @Column(name = "available_tokens", nullable = false)
    private Integer availableTokens;

    @NotNull
    @Column(name = "last_refill_time", nullable = false)
    private Instant lastRefillTime;

    @PrePersist
    public void onCreate() {
        lastRefillTime = Instant.now();
    }
}