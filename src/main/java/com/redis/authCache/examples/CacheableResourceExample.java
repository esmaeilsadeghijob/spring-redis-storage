package com.redis.authCache.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheableResourceExample {


    Logger logger = LoggerFactory.getLogger(CacheableResourceExample.class);

    @Cacheable("data")
    public String getSomeData(){

        logger.info("accessing the data, NOT FROM CACHE");

        return "someData";

    }


}
