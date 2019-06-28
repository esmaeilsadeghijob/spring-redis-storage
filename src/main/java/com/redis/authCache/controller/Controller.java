package com.redis.authCache.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.authCache.entity.Driver;
import com.redis.authCache.entity.Vehicle;
import com.redis.authCache.examples.CacheableResourceExample;
import com.redis.authCache.repository.DriverRepo;
import com.redis.authCache.repository.VehicleRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class Controller {

    @Autowired
    VehicleRepo vehicleRepo;

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    CacheableResourceExample cacheableResourceExample;

    @GetMapping("/vehicle")
    public Vehicle ret(){

        Driver driver = new Driver(2L, "cemal", "turkoglu");
        Vehicle vehicle = new Vehicle(1L, "34 XA 102", "opel", "astra",driver);

        vehicleRepo.save(vehicle);

        return vehicle;
    }

    @GetMapping("/getAll")
    public Iterable<Vehicle> retAll(){

        return vehicleRepo.findAll();

    }

    @GetMapping("/driver")
    public Driver driver(){

        Driver driver = new Driver(2L, "cemal", "turkoglu");

        driverRepo.save(driver);

        return driver;

    }

    @GetMapping("/getFromCache")
    public void getFromCache(){


        cacheableResourceExample.getSomeData();
        cacheableResourceExample.getSomeData();
        cacheableResourceExample.getSomeData();

    }
}
