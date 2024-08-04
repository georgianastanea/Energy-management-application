FROM openjdk:17

#Add the application's jar to the container
COPY /target/monitoring-service-0.0.1-SNAPSHOT.jar /folder/app.jar

#execute the application
ENTRYPOINT ["java", "-jar", "/folder/app.jar"]
