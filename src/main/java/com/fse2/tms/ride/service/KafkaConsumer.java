package com.fse2.tms.ride.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fse2.tms.ride.entity.RideEntity;
import com.fse2.tms.ride.repo.RideRepo;

@Service
public class KafkaConsumer {
	
	@Autowired
	private RideRepo rideRepo;
	
	@KafkaListener(topics = "${kafka.add-ride}", groupId = "${kafka.group-id}")
	public void consumeAddRide(String message) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		RideEntity rideRecord = mapper.readValue(message, RideEntity.class); 
		rideRepo.addRide(rideRecord);
	}
	
	@KafkaListener(topics = "${kafka.update-ride}", groupId = "${kafka.group-id}")
	public void consumeUpdateRide(String message) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		RideEntity rideRecord = mapper.readValue(message, RideEntity.class);
		String driverid = "-1";
		if (rideRecord.getDriverid() != null) {
			driverid = rideRecord.getDriverid();
		}
		rideRepo.updateRide(rideRecord.getRideid(), rideRecord.getRidedate(), rideRecord.getRidestatus(), driverid);
	}
}
