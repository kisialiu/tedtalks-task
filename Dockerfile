# Builder
FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Application
FROM eclipse-temurin:21

WORKDIR /app
COPY --from=build /app/target/*.jar tedtalks-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/app/tedtalks-0.0.1-SNAPSHOT.jar"]
