package com.RateLimiter.controllers;

import com.RateLimiter.dtos.CreateUserDTO;
import com.RateLimiter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity createUser(@RequestBody CreateUserDTO user) {
        try {
            validateUser(user);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    private void validateUser(CreateUserDTO user) {
        String userName = user.getUserName();
        String userPhoneNumber = user.getPhoneNumber();
        String userEmailId = user.getEmailId();
        String userPassword = user.getPassword();

        if ((userName == null) || (userName.trim().isEmpty())) {
            throw new IllegalArgumentException("Invalid Username");
        }
        if ((userPhoneNumber == null) || (userPhoneNumber.trim().isEmpty())) {
            throw new IllegalArgumentException("Invalid Phone Number");
        }
        if ((userEmailId == null) || (userEmailId.trim().isEmpty()) || !isValidEmail(userEmailId)) {
            throw new IllegalArgumentException("Invalid Email Id");
        }
        if ((userPassword == null) || (userPassword.trim().length() > 16) || (userPassword.trim().length() < 8)) {
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
