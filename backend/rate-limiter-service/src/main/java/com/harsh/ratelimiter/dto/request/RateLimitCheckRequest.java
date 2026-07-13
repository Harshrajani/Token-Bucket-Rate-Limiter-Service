package com.harsh.ratelimiter.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RateLimitCheckRequest {

    @NotBlank
    private String clientId;

}