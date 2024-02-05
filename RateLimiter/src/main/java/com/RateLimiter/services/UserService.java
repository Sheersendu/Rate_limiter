package com.RateLimiter.services;

import com.RateLimiter.dtos.CreateUserDTO;
import com.RateLimiter.dtos.UserLoginDTO;
import com.RateLimiter.models.User;
import com.RateLimiter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.RateLimiter.Utilities.PasswordEncoder.hashPassword;
import static com.RateLimiter.Utilities.PasswordEncoder.passwordMatches;


@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserDTO user) {
        String hashedPassword = hashPassword(user.getPassword());
        User newUser = User.builder()
                .userName(user.getUserName())
                .emailId(user.getEmailId())
                .phoneNumber(user.getPhoneNumber())
                .password(hashedPassword)
                .build();
        return userRepository.save(newUser);
    }

    public Boolean loginUser(UserLoginDTO user) {
        String emailId = user.getEmailId().trim();
        User matchedUser = userRepository.findByEmailId(emailId);
        if (matchedUser != null) {
            return passwordMatches(user.getPassword(), matchedUser.getPassword());
        }
        return false;
    }
}
