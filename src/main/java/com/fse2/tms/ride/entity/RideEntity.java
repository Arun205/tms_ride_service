package com.fse2.tms.ride.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="ridelog")
public class RideEntity {
	
	private String rideid;
	private String riderid;
	private String driverid;
	private String ridedate;
	private String pickup;
	private String drop;
	private String ridestatus;
	
	@DynamoDBHashKey(attributeName="rideid")
	@DynamoDBAutoGeneratedKey
	public String getRideid() {
		return rideid;
	}
	@DynamoDBAttribute
	public String getRiderid() {
		return riderid;
	}
	@DynamoDBAttribute
	public String getDriverid() {
		return driverid;
	}
	public void setDriverid(String driverid) {
		this.driverid = driverid;
	}
	@DynamoDBRangeKey(attributeName="ridedate")
	public String getRidedate() {
		return ridedate;
	}
	@DynamoDBAttribute
	public String getPickup() {
		return pickup;
	}
	@DynamoDBAttribute
	public String getDrop() {
		return drop;
	}
	@DynamoDBAttribute
	public String getRidestatus() {
		return ridestatus;
	}
	
	public RideEntity(String rideid, String riderid, String driverid, String ridedate, String pickup, String drop, String ridestatus) {
		this.rideid = rideid;
		this.riderid = riderid;
		this.driverid = driverid;
		this.ridedate = ridedate;
		this.pickup = pickup;
		this.drop = drop;
		this.ridestatus = ridestatus;
	}
	public RideEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

}