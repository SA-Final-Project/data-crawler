FROM openjdk:17-jdk-alpine AS build
LABEL maintainer="EphremG"
EXPOSE 8081
RUN apk add --no-cache maven
WORKDIR /app
COPY . /app
RUN mvn package -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/ /app/target/
RUN mv /app/target/*.jar /app/target/rtds-server.jar
EXPOSE 8081

# Run the jar file
ENTRYPOINT ["java","-jar","/app/target/rtds-server.jar","https://swapi.dev/api/people"]
