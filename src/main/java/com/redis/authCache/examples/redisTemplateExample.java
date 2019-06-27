package com.redis.authCache.examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class redisTemplateExample {

    @Autowired
    RedisTemplate redisTemplate;

    public void runRedisCommandsViaRedisTemplate(){

        // string

        redisTemplate.opsForValue().set("hello", "world");

        redisTemplate.opsForValue().set("myKey","myValue");

        String hello = (String) redisTemplate.opsForValue().get("hello");
        String key = (String) redisTemplate.opsForValue().get("myKey");

        System.out.println(hello);
        System.out.println(key);

        // list

        redisTemplate.opsForList().leftPush("myList","value1");

        // hash

        redisTemplate.opsForHash().put("myHash","hashKey","hashVal");

        // set

        redisTemplate.opsForSet().add("mySet", "val1","val2","val3");

    }

}
