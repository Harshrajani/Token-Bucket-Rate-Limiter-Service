package com.harsh.ratelimiter.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harsh.ratelimiter.dto.request.CreatePolicyRequest;
import com.harsh.ratelimiter.dto.response.PolicyResponse;
import com.harsh.ratelimiter.service.PolicyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/policies")
@RequiredArgsConstructor
@Validated
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping
    public ResponseEntity<PolicyResponse> createPolicy(
            @Valid @RequestBody CreatePolicyRequest request) {

        PolicyResponse response = policyService.createPolicy(request);

        return ResponseEntity
                .created(URI.create("/api/v1/admin/policies/" + response.getClientId()))
                .body(response);
    }

}