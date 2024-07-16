# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

#VOLUME /tmp

# Define the JAR file name
ARG JAR_FILE=build/libs/2024b.yarden.cherry-final.jar

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Make port 80 available to the world outside this container
EXPOSE 80

# Run the JAR file
ENTRYPOINT ["java","-jar","/app.jar"]