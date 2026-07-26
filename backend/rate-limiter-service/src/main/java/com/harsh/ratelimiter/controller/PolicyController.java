package com.harsh.ratelimiter.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harsh.ratelimiter.dto.request.CreatePolicyRequest;
import com.harsh.ratelimiter.dto.request.UpdatePolicyRequest;
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

    @GetMapping("/{clientId}")
    public ResponseEntity<PolicyResponse> getPolicy(
            @PathVariable String clientId) {

        PolicyResponse response = policyService.getPolicy(clientId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<PolicyResponse> updatePolicy(
            @PathVariable String clientId,
            @Valid @RequestBody UpdatePolicyRequest request) {

        PolicyResponse response =
                policyService.updatePolicy(clientId, request);

        return ResponseEntity.ok(response);
    }

}