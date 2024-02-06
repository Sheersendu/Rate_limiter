package com.RateLimiter.services;

import com.RateLimiter.dtos.CreateUserDTO;
import com.RateLimiter.dtos.UserLoginDTO;
import com.RateLimiter.models.User;
import com.RateLimiter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

import static com.RateLimiter.Utilities.PasswordEncoder.hashPassword;
import static com.RateLimiter.Utilities.PasswordEncoder.passwordMatches;


@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    private Jedis jedis;

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
        User createdUser = userRepository.save(newUser);
        setRedisUserKey(user.getUserName());
        return createdUser;
    }

    public Boolean loginUser(UserLoginDTO user) {
        String emailId = user.getEmailId().trim();
        User matchedUser = userRepository.findByEmailId(emailId);
        if (matchedUser != null) {
            return passwordMatches(user.getPassword(), matchedUser.getPassword());
        }
        return false;
    }

    private void setRedisUserKey(String userName) {
        Map<String, String> userRateLimitingInfo = new HashMap<>() {
        };
        long timestamp = System.currentTimeMillis() / 1000;
        userRateLimitingInfo.put("token", "10");
        userRateLimitingInfo.put("lastRequestTimestamp", String.valueOf(timestamp));
        this.jedis.hmset(userName, userRateLimitingInfo);
    }
}
