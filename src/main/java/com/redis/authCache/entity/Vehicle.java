package com.redis.authCache.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.annotation.Generated;
import javax.persistence.Entity;

@RedisHash("vehicle")
public class Vehicle {

    @Id
    private long id;

    private String numberPlate;
    private String make;
    private String model;
    private Driver driver;


    public Vehicle() {
    }

    public Vehicle(long id, String numberPlate, String make, String model, Driver driver) {
        this.id = id;
        this.numberPlate = numberPlate;
        this.make = make;
        this.model = model;
        this.driver = driver;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", numberPlate='" + numberPlate + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}
