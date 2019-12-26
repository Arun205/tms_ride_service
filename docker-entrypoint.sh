#!/bin/bash
exec java -jar -Deureka.datacenter=cloud -Dspring.profiles.active=aws tms_ride_service-0.0.1-SNAPSHOT.jar