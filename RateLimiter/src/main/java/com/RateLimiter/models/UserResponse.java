package com.RateLimiter.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import enums.UserResponseStatus;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class UserResponse {
    @JsonProperty("status")
    private UserResponseStatus status;

    @JsonProperty("data")
    private User data = null;

    @JsonProperty("message")
    private String message = "";
}
