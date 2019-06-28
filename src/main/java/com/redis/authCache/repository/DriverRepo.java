package com.redis.authCache.repository;


import com.redis.authCache.entity.Driver;
import com.redis.authCache.entity.Vehicle;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepo extends CrudRepository<Driver,Long> {

}
