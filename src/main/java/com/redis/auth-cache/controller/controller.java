package com.redis.example.controller;

import com.redis.example.entity.Vehicle;
import com.redis.example.repository.VehicleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {

    @Autowired
    VehicleRepo vehicleRepo;

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
}
