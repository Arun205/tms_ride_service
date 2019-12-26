package com.fse2.tms.ride;

import static org.mockito.Mockito.doNothing;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.fse2.tms.ride.repo.RideRepo;
import com.fse2.tms.ride.service.KafkaConsumer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KafkaConsumer.class)
@Import(DynamoDBConfigLocal.class)
public class TmsRideRideServiceTests {
	
	@MockBean
	private RideRepo rideRepo;
	
	@Autowired
	private KafkaConsumer kafkaConsumer;
	
	@Test
	public void consumeAddRideTest( ) throws IOException {
		kafkaConsumer.consumeAddRide("{\"riderid\":\"1\"}");
	}
	
	@Test
	public void consumeUpdateRideTest( ) throws IOException {
		kafkaConsumer.consumeUpdateRide("{\"riderid\":\"1\"}");
	}

}
