package com.RateLimiter.controllers;

import com.RateLimiter.dtos.CreateUserDTO;
import com.RateLimiter.models.User;
import com.RateLimiter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody CreateUserDTO user) {
        validateUser(user);
        return userService.createUser(user);
    }

    private void validateUser(CreateUserDTO user) {
        String userName = user.getUserName().trim();
        String userPhoneNumber = user.getPhoneNumber().trim();
        String userEmailId = user.getEmailId().trim();
        String userPassword = user.getPassword().trim();

        if (userName.isEmpty()) {
            throw new IllegalArgumentException("Invalid Username");
        }
        if (userPhoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Invalid Phone Number");
        }
        if ((userEmailId.isEmpty()) || !isValidEmail(userEmailId)) {
            throw new IllegalArgumentException("Invalid Email Id");
        }
        if ((userPassword.length() > 16) || (userPassword.length() < 8)) {
            throw new IllegalArgumentException("Password must be between 8-16 characters long");
        }
    }

    private boolean isValidEmail(String emailId) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailId);
        return matcher.matches();
    }
}
