package com.RateLimiter.Utilities;

public class Constants {
    public static Integer redisKeyExpirationInSeconds = 1800;
    public static Integer redisTokenExpirationInSeconds = 60;
    public static String redisTokenKey = "token";

    public static Integer redisTotalTokens = 10;
    public static String redisRequestTimestampKey = "lastRequestTimestamp";
}
