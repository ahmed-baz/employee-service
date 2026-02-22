# -------- Build Stage --------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# -------- Runtime Stage --------
FROM eclipse-temurin:17-jdk-alpine

LABEL maintainer="developer.baz@gmail.com"

WORKDIR /app

ARG PROFILE=dev
ENV ACTIVE_PROFILE=$PROFILE

COPY --from=build /build/target/*.jar employee-service.jar

EXPOSE 2222

ENTRYPOINT ["sh","-c","java -jar -Dspring.profiles.active=$ACTIVE_PROFILE employee-service.jar"]