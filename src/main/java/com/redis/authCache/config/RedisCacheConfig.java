package com.redis.authCache.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Autowired
    JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public RedisCacheManager cacheManager() {
        return RedisCacheManager.create(jedisConnectionFactory);
    }
}
