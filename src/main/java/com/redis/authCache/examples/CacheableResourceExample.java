package com.redis.authCache.examples;

import org.springframework.cache.annotation.Cacheable;

public class CacheableResourceExample {

    @Cacheable
    public String getSomeData(){

        System.out.println("accessing the data from end resource");

        return "someData";

    }

    public void testCachable(){

        System.out.println(getSomeData()); // first time from end-resource (db)

        System.out.println(getSomeData()); // from cache anymore

        System.out.println(getSomeData());


    }

}
