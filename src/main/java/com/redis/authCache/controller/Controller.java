package com.redis.authCache.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.authCache.entity.Driver;
import com.redis.authCache.entity.Vehicle;
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

    @GetMapping("/vehicle")
    public Vehicle ret(){

        Vehicle vehicle = new Vehicle(1L, "numberPlate", "make", "model");

        vehicleRepo.save(vehicle);

        return vehicle;
    }

    @GetMapping("/getAll")
    public Iterable<Vehicle> retAll(){

        return vehicleRepo.findAll();

    }

    @GetMapping("/driver")
    public Driver driver(){

        Driver driver = new Driver(2L, "object", "mapper");

        //driverRepo.save(driver);




//        ObjectMapper objectMapper = new ObjectMapper();
//        redisTemplate.opsForHash().putAll("driver", objectMapper.convertValue(driver,Map.class));

        return driver;

    }
}
