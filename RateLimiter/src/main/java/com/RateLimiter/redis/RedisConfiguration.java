package com.RateLimiter.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfiguration {
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private Integer redisPort;
    @Value("${redis.database}")
    private Integer redisDatabase;
    @Value("${redis.timeout}")
    private Integer redisTimeout;

    @Bean
    public Jedis jedis() {
        Jedis jedis = new Jedis(redisHost, Integer.parseInt(String.valueOf(redisPort)), redisTimeout);
        jedis.select(redisDatabase);
        return jedis;
    }
}
