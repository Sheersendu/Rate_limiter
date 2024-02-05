package com.RateLimiter.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import enums.UserResponseStatus;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class UserResponse {
    @JsonProperty("Status")
    private UserResponseStatus Status;

    @JsonProperty("message")
    private String message;
}
