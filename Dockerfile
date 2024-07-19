# Stage 1: Build stage i.e. install maven dependancies
FROM maven:latest AS build

WORKDIR /app

# Copy source code
COPY . .

# Build the application
RUN mvn clean package -DskipTests

FROM openjdk:17

COPY --from=build /app/target/payment-api.jar /payment-api.jar

ENTRYPOINT ["java", "-jar", "/payment-api.jar"]