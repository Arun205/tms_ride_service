package com.fse2.tms.ride;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fse2.tms.ride.controller.RideController;
import com.fse2.tms.ride.entity.RideEntity;
import com.fse2.tms.ride.repo.RideRepo;

@RunWith(SpringRunner.class)
@WebMvcTest(RideController.class)
public class TmsRideServiceApplicationTests {
	
	@Autowired
	MockMvc mockMvc;
	MvcResult mvcResult;
	
	@Autowired
	RideController rideController;
	
	@MockBean
	private RideRepo rideRepo;
	
	@Test
	public void getRiderRidesTest() throws Exception {
		List<RideEntity> allRides = new ArrayList<>();
		RideEntity ride1 = new RideEntity();
		ride1.setDriverid("1");
		allRides.add(ride1);
		Mockito.when(rideRepo.getRiderRides(ArgumentMatchers.anyString())).thenReturn(allRides);
		this.mockMvc.perform(get("/rider").param("riderid", "1"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getDriverRidesTest() throws Exception {
		List<RideEntity> allRides = new ArrayList<>();
		RideEntity ride1 = new RideEntity();
		ride1.setDriverid("1");
		allRides.add(ride1);
		Mockito.when(rideRepo.getDriverRides(ArgumentMatchers.anyString())).thenReturn(allRides);
		this.mockMvc.perform(get("/driver").param("driverid", "1"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getAllRidesTest() throws Exception {
		List<RideEntity> allRides = new ArrayList<>();
		RideEntity ride1 = new RideEntity();
		ride1.setDriverid("1");
		allRides.add(ride1);
		Mockito.when(rideRepo.getDriverRides(ArgumentMatchers.anyString())).thenReturn(allRides);
		this.mockMvc.perform(get("/admin"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getOpenRidesTest() throws Exception {
		List<RideEntity> allRides = new ArrayList<>();
		RideEntity ride1 = new RideEntity();
		ride1.setDriverid("1");
		allRides.add(ride1);
		Mockito.when(rideRepo.getDriverRides(ArgumentMatchers.anyString())).thenReturn(allRides);
		this.mockMvc.perform(get("/openrides"))
				.andExpect(status().isOk());
	}

}
