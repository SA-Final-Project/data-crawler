FROM maven:3.8.4-openjdk-17 as build
WORKDIR /app
COPY pom.xml ./

COPY src src
RUN mvn package -DskipTests

FROM azul/zulu-openjdk-alpine:17
WORKDIR /app
COPY --from=build /app/target/ConfigServer-0.0.1-SNAPSHOT.jar /app/configserver.jar
EXPOSE 7000
CMD ["java", "-jar", "configserver.jar"]
