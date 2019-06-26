package com.redis.example;

import com.redis.example.entity.Vehicle;
import com.redis.example.repository.VehicleRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleApplicationTests {

	@Autowired
	VehicleRepo vehicleRepo;

	@Test
	public void contextLoads() {

        Vehicle vehicle = new Vehicle();

        vehicle.setId(2L);
        vehicle.setMake("BMW");
        vehicle.setModel("Sth");
        vehicle.setNumberPlate("34 CT 18");
        //vehicle.setDriver(new Driver(1L,"cemal","turkoglu"));



        vehicleRepo.save(vehicle);


        //System.out.println(vehicleRepo.findById(1L).get().toString());

    }

}
