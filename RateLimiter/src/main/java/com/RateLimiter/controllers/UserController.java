package com.RateLimiter.controllers;

import com.RateLimiter.dtos.CreateUserDTO;
import com.RateLimiter.dtos.UserLoginDTO;
import com.RateLimiter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity createUser(@RequestBody CreateUserDTO user) {
        try {
            validateNewUser(user);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping("/login")
    public ResponseEntity loginUser(@RequestBody UserLoginDTO user) {
        try {
            validateExistingUser(user);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.loginUser(user));
    }

    private void validateExistingUser(UserLoginDTO user) {
        String userEmailId = user.getEmailId();
        String userPassword = user.getPassword();
        if (!isValidEmail(userEmailId)) {
            throw new IllegalArgumentException("Invalid Email Id");
        }
        if (!isValidPassword(userPassword)) {
            throw new IllegalArgumentException("Password must be between 8-16 characters long");
        }
    }

    private void validateNewUser(CreateUserDTO user) {
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
        if (!isValidEmail(userEmailId)) {
            throw new IllegalArgumentException("Invalid Email Id");
        }
        if (!isValidPassword(userPassword)) {
            throw new IllegalArgumentException("Password must be between 8-16 characters long");
        }
    }

    private boolean isValidEmail(String emailId) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

        if ((emailId == null) || (emailId.trim().isEmpty())) return false;
        Matcher matcher = pattern.matcher(emailId);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        return (password != null) && (password.trim().length() <= 16) && (password.trim().length() >= 8);
    }
}
