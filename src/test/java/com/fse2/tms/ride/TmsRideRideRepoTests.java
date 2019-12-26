package com.fse2.tms.ride;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fse2.tms.ride.entity.RideEntity;
import com.fse2.tms.ride.repo.RideRepo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RideRepo.class)
@Import(DynamoDBConfigLocal.class)
public class TmsRideRideRepoTests {
	
	@Autowired
	RideRepo rideRepo;
	
	private static DynamoDBMapper mapper;
	private static AmazonDynamoDB amazonDynamoDB;
	private static DynamoDB dynamoDB;
	
	private static String TABLENAME = "ridelogtest";
	private static String ATTRIBUTE1 = "rideid";
	private static String ATTRIBUTE2 = "ridedate";
	private static String ATTRIBUTE3 = "riderid";
	private static String ATTRIBUTE4 = "driverid";
	private static String ATTRIBUTE5 = "ridestatus";
	
	@Before
    public void setupClass() throws InterruptedException {
        amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                .build();
        mapper = new DynamoDBMapper(amazonDynamoDB);
        dynamoDB = new DynamoDB(amazonDynamoDB);
        
        Table table = dynamoDB.getTable(TABLENAME);
        try {
            System.out.println("Issuing DeleteTable request for " + TABLENAME);
            table.delete();

            System.out.println("Waiting for " + TABLENAME + " to be deleted...this may take a while...");

            table.waitForDelete();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        
        ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
        tableKeySchema.add(new KeySchemaElement().withAttributeName(ATTRIBUTE1).withKeyType(KeyType.HASH));
        tableKeySchema.add(new KeySchemaElement().withAttributeName(ATTRIBUTE2).withKeyType(KeyType.RANGE));
        
        attributeDefinitions.add(new AttributeDefinition(ATTRIBUTE1, ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition(ATTRIBUTE2, ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition(ATTRIBUTE3, ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition(ATTRIBUTE4, ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition(ATTRIBUTE5, ScalarAttributeType.S));
        
        ArrayList<GlobalSecondaryIndex> globalSecondaryIndex = new ArrayList<GlobalSecondaryIndex>();
        GlobalSecondaryIndex riderid = new GlobalSecondaryIndex()
        	    .withIndexName(ATTRIBUTE3)
        	    .withProvisionedThroughput(new ProvisionedThroughput()
        	        .withReadCapacityUnits((long) 1)
        	        .withWriteCapacityUnits((long) 1))
        	        .withProjection(new Projection().withProjectionType(ProjectionType.ALL));
        ArrayList<KeySchemaElement> rideridKeySchema = new ArrayList<KeySchemaElement>();
        rideridKeySchema.add(new KeySchemaElement()
        	    .withAttributeName(ATTRIBUTE3)
        	    .withKeyType(KeyType.HASH));
        riderid.setKeySchema(rideridKeySchema);
        
        GlobalSecondaryIndex driverid = new GlobalSecondaryIndex()
        	    .withIndexName(ATTRIBUTE4)
        	    .withProvisionedThroughput(new ProvisionedThroughput()
        	        .withReadCapacityUnits((long) 1)
        	        .withWriteCapacityUnits((long) 1))
        	        .withProjection(new Projection().withProjectionType(ProjectionType.ALL));
        ArrayList<KeySchemaElement> driveridKeySchema = new ArrayList<KeySchemaElement>();
        driveridKeySchema.add(new KeySchemaElement()
        	    .withAttributeName(ATTRIBUTE4)
        	    .withKeyType(KeyType.HASH));
        driverid.setKeySchema(driveridKeySchema);
        
        GlobalSecondaryIndex ridestatus = new GlobalSecondaryIndex()
        	    .withIndexName(ATTRIBUTE5)
        	    .withProvisionedThroughput(new ProvisionedThroughput()
        	        .withReadCapacityUnits((long) 1)
        	        .withWriteCapacityUnits((long) 1))
        	        .withProjection(new Projection().withProjectionType(ProjectionType.ALL));
        ArrayList<KeySchemaElement> ridestatusKeySchema = new ArrayList<KeySchemaElement>();
        ridestatusKeySchema.add(new KeySchemaElement()
        	    .withAttributeName(ATTRIBUTE5)
        	    .withKeyType(KeyType.HASH));
        ridestatus.setKeySchema(ridestatusKeySchema);
        
        globalSecondaryIndex.add(riderid);
        globalSecondaryIndex.add(driverid);
        globalSecondaryIndex.add(ridestatus);
        
        CreateTableRequest createTableRequest = new CreateTableRequest()
        	    .withTableName(TABLENAME)
        	    .withProvisionedThroughput(new ProvisionedThroughput()
        	        .withReadCapacityUnits((long) 5)
        	        .withWriteCapacityUnits((long) 1))
        	    .withAttributeDefinitions(attributeDefinitions)
        	    .withKeySchema(tableKeySchema)
        	    .withGlobalSecondaryIndexes(globalSecondaryIndex);
        
        table = dynamoDB.createTable(createTableRequest);
        table.waitForActive();
        RideEntity ride = new RideEntity("1","1","1","1","1","1","1");
		rideRepo.addRide(ride);
        
        System.out.println(table.getDescription());
            
    }
		
	@Test
	public void addRideTest() throws Exception {
		RideEntity ride = new RideEntity("1","1","1","1","1","1","1");
		rideRepo.addRide(ride);
	}
	
	@Test
	public void getRiderRidesTest() throws JsonParseException, JsonMappingException, IOException {
		rideRepo.getRiderRides("1");
	}
	
	@Test
	public void getDriverRidesTest() throws JsonParseException, JsonMappingException, IOException {
		rideRepo.getDriverRides("1");
	}
	@Test
	public void getAllRidesTest() throws JsonParseException, JsonMappingException, IOException {
		rideRepo.getAllRides();
	}
	@Test
	public void getOpenRidesTest() throws JsonParseException, JsonMappingException, IOException {
		rideRepo.getOpenRides();
	}
	@Test
	public void updateRideTest() throws JsonParseException, JsonMappingException, IOException {
		rideRepo.updateRide("1","1","1","1");
	}
}
