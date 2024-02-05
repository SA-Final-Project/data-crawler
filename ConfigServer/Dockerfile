FROM openjdk:17-jdk-alpine

# Add Maintainer Info
LABEL maintainer="EphremG"

# Make port 8888 available to the world outside this container
EXPOSE 8888

# The application's jar file
ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} config-server.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/config-server.jar"]