# Use an official Maven image to build the project
FROM maven:3.8.1-openjdk-11 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/change-data-capture-service-1.0.0.jar ./change-data-capture-service-1.0.0.jar
ENTRYPOINT ["java", "-jar", "./change-data-capture-service-1.0.0.jar"]
