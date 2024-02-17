package com.RateLimiter.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import static com.RateLimiter.Utilities.Constants.*;
import static java.lang.Math.max;

@Component
public class RateLimiter {

    private final Jedis jedis;
    @Autowired
    public RateLimiter(Jedis jedis) {
        this.jedis = jedis;
    }

    private void refreshToken(String userName) {
        long lastRequestTimestamp = Long.parseLong(this.jedis.hget(userName, redisRequestTimestampKey));
        long currentTimestamp = System.currentTimeMillis() / 1000;
        long timestampDifference = currentTimestamp - lastRequestTimestamp;
        if (timestampDifference > redisTokenExpirationInSeconds) {
            this.jedis.hset(userName, redisTokenKey, String.valueOf(redisTotalTokens));
            this.jedis.hset(userName, redisRequestTimestampKey, String.valueOf(currentTimestamp));
        }
    }

    public boolean processUserRequest(String userName) {
        refreshToken(userName);
        int userTokens = Integer.parseInt(this.jedis.hget(userName, redisTokenKey)) - 1;
        this.jedis.hset(userName, redisTokenKey, String.valueOf(max(0, userTokens)));
        return userTokens > 0;
    }
}
