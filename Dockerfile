FROM openjdk:8
COPY /target/tms_ride_service-0.0.1-SNAPSHOT.jar /
COPY /docker-entrypoint.sh /
EXPOSE 2222
ENTRYPOINT ["/docker-entrypoint.sh"]