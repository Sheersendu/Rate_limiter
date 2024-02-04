package com.RateLimiter.services;

import com.RateLimiter.dtos.CreateUserDTO;
import com.RateLimiter.models.User;
import com.RateLimiter.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserDTO user) {
        User newUser = User.builder()
                .userName(user.getUserName())
                .emailId(user.getEmailId())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .build();
        return userRepository.save(newUser);
    }
}
