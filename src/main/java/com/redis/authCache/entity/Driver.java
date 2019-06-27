package com.redis.authCache.entity;

import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

@RedisHash
public class Driver {

    private long id;
    private String name;
    private String surname;

    public Driver(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Driver() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
