FROM openjdk:17-jdk-alpine
# Add Maintainer Info
LABEL maintainer="EphremG"

EXPOSE 8761

# The application's jar file
ARG JAR_FILE=target/*.jar

# Add the application's jar to the container
ADD ${JAR_FILE} eureka-server.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/eureka-server.jar"]