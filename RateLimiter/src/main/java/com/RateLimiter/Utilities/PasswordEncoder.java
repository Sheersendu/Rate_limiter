package com.RateLimiter.Utilities;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static Boolean passwordMatches(String userPassword, String hashedPassword) {
        return BCrypt.checkpw(userPassword, hashedPassword);
    }
}
