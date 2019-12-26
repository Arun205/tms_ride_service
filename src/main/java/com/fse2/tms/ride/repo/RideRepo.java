package com.fse2.tms.ride.repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fse2.tms.ride.entity.RideEntity;

@Repository
public class RideRepo {
	
	private Logger LOGGER = LoggerFactory.getLogger(RideRepo.class);
	
	@Autowired
	private DynamoDBMapper mapper;
	
	@Autowired
	private AmazonDynamoDB amazonDynamoDB;
	
	private DynamoDB dynamoDB;
	
	private static final String TABLENAME = "ridelog";
	
	public void addRide(RideEntity ride) {
		mapper.save(ride);
	}
	
	public List<RideEntity> getRiderRides(String riderid) throws JsonMappingException, IOException {
		dynamoDB = new DynamoDB(amazonDynamoDB);
		Table table = dynamoDB.getTable(TABLENAME);
		Index index = table.getIndex("riderid");
		
		QuerySpec spec = new QuerySpec()
				.withKeyConditionExpression("riderid = :a")
				.withValueMap(new ValueMap()
						.withString(":a", riderid));
		ItemCollection<QueryOutcome> items = index.query(spec);
		
		return formatItems(items);
	}
	
	public List<RideEntity> getDriverRides(String driverid) throws JsonMappingException, IOException {
		dynamoDB = new DynamoDB(amazonDynamoDB);
		Table table = dynamoDB.getTable(TABLENAME);
		Index index = table.getIndex("driverid");
		
		QuerySpec spec = new QuerySpec()
				.withKeyConditionExpression("driverid = :a")
				.withValueMap(new ValueMap()
						.withString(":a", driverid));
		ItemCollection<QueryOutcome> items = index.query(spec);  

		return formatItems(items);

	}
	
	public List<RideEntity> getOpenRides() throws JsonMappingException, IOException {
		dynamoDB = new DynamoDB(amazonDynamoDB);
		Table table = dynamoDB.getTable(TABLENAME);
		Index index = table.getIndex("ridestatus");
		
		QuerySpec spec = new QuerySpec()
				.withKeyConditionExpression("ridestatus = :a")
				.withValueMap(new ValueMap()
						.withString(":a", "Searching for Cab"));
		ItemCollection<QueryOutcome> items = index.query(spec);
		return formatItems(items);
	}
	
	public List<RideEntity> getAllRides() {
		ScanRequest scanRequest = new ScanRequest()
			    .withTableName(TABLENAME);
		ScanResult result = amazonDynamoDB.scan(scanRequest);
		List<RideEntity> allRides = new ArrayList<RideEntity>();
		for (Map<String, AttributeValue> item : result.getItems()){
			AttributeValue rideidAV = item.getOrDefault("rideid", new AttributeValue());
		    String rideid = rideidAV.getS();
		    System.out.println(rideid);
		    AttributeValue dropAV = item.getOrDefault("drop", new AttributeValue());
		    String drop = dropAV.getS();
		    AttributeValue ridedateAV = item.getOrDefault("ridedate", new AttributeValue());
		    String ridedate = ridedateAV.getS();
		    AttributeValue driveridAV = item.getOrDefault("driverid", new AttributeValue());
		    String driverid = driveridAV.getS();
		    AttributeValue pickupAV = item.getOrDefault("pickup", new AttributeValue());
		    String pickup = pickupAV.getS();
		    AttributeValue rideridAV = item.getOrDefault("riderid", new AttributeValue());
		    String riderid = rideridAV.getS();
		    AttributeValue ridestatusAV = item.getOrDefault("ridestatus", new AttributeValue());
		    String ridestatus = ridestatusAV.getS();
		    RideEntity record = new RideEntity(rideid, riderid, driverid, ridedate, pickup, drop, ridestatus);
		    allRides.add(record);
		}
		return allRides;
	}
	
	public String updateRide(String rideid, String ridedate, String ridestatus, String driverid) {
		DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table table = dynamoDB.getTable(TABLENAME);
        
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("rideid", rideid, "ridedate", ridedate)
                .withUpdateExpression("set ridestatus = :ridestatus, driverid = :driverid")
                .withValueMap(new ValueMap()
                		.withString(":ridestatus", ridestatus)
                		.withString(":driverid", driverid))
                .withReturnValues(ReturnValue.UPDATED_NEW);

            try {
                table.updateItem(updateItemSpec);
            }
            catch (Exception e) {
            	LOGGER.debug(e.getMessage());
            }
            
		return "success";
	}
	
	public List<RideEntity> formatItems(ItemCollection<QueryOutcome> items) {
		ObjectMapper objmapper = new ObjectMapper();
		List<RideEntity> allRides = new ArrayList<RideEntity>();
		items.forEach(item -> {
			RideEntity rideRecord;
			try {
				rideRecord = objmapper.readValue(item.toJSON(), RideEntity.class);
				allRides.add(rideRecord);
			} catch (JsonParseException e) {
				LOGGER.debug(e.getMessage());
			} catch (JsonMappingException e) {
				LOGGER.debug(e.getMessage());
			} catch (IOException e) {
				LOGGER.debug(e.getMessage());
			}
		});
		return allRides;
	}
}

