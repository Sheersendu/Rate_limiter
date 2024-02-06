package com.RateLimiter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginDTO {
    private String emailId;
    private String password;
}
