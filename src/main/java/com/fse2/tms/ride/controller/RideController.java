package com.fse2.tms.ride.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fse2.tms.ride.entity.RideEntity;
import com.fse2.tms.ride.repo.RideRepo;

@RestController
public class RideController {
	
	@Autowired
	private RideRepo rideRepo;
	
	@GetMapping("/admin")
	public List<RideEntity> getAllRides() {
		return rideRepo.getAllRides();
	}
	
	@GetMapping("/rider")
	public List<RideEntity> getRiderRides(@RequestParam String riderid) throws JsonParseException, JsonMappingException, IOException {
		return rideRepo.getRiderRides(riderid);
	}

	@GetMapping("/driver")
	public List<RideEntity> getDriverRides(@RequestParam String driverid) throws JsonParseException, JsonMappingException, IOException {
		return rideRepo.getDriverRides(driverid);
	}
	
	@GetMapping("/openrides")
	public List<RideEntity> getOpenRides() throws JsonParseException, JsonMappingException, IOException {
		return rideRepo.getOpenRides();
	}
}
