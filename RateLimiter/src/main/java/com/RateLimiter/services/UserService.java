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

import static com.RateLimiter.Utilities.Constants.*;
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

    public User loginUser(UserLoginDTO user) {
        String emailId = user.getEmailId().trim();
        User matchedUser = userRepository.findByEmailId(emailId);
        if ((matchedUser == null) || (!passwordMatches(user.getPassword(), matchedUser.getPassword()))) {
            throw new IllegalArgumentException("Invalid user");
        }
        setRedisUserKey(matchedUser.getUserName());
        return matchedUser;
    }

    public User getUserDetails(String userName) {
        String trimmedUserName = userName.trim();
        User user = userRepository.findByUserName(trimmedUserName);
        if (user == null) {
            throw new IllegalArgumentException(String.format("No user with username '%s' found", userName));
        }
        return user;
    }

    private void setRedisUserKey(String userName) {
        Map<String, String> userRateLimitingInfo = new HashMap<>() {
        };
        long timestamp = System.currentTimeMillis() / 1000;
        userRateLimitingInfo.put(redisTokenKey, String.valueOf(redisTotalTokens));
        userRateLimitingInfo.put(redisRequestTimestampKey, String.valueOf(timestamp));
        this.jedis.hmset(userName, userRateLimitingInfo);
        this.jedis.expire(userName, redisKeyExpirationInSeconds);
    }
}
