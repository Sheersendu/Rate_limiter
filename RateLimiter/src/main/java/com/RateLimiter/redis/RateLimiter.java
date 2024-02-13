package com.RateLimiter.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class RateLimiter {

    @Autowired
    private static Jedis jedis;

    private static void refreshToken(String userName) {
        long lastRequestTimestamp = Long.parseLong(jedis.hget(userName, "lastRequestTimestamp"));
        long currentTimestamp = System.currentTimeMillis() / 1000;
        long timestampDifference = currentTimestamp - lastRequestTimestamp;
        if (timestampDifference > 60) {
            jedis.hset(userName, "token", "10");
        }
    }

    public static boolean processUserRequest(String userName) {
        refreshToken(userName);
        int userTokens = Integer.parseInt(jedis.hget(userName, "token"));
        return userTokens > 0;
    }
}
