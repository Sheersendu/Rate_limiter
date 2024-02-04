package com.RateLimiter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateUserDTO {
    private String userName;
    private String emailId;
    private String phoneNumber;
    private String password;
}
