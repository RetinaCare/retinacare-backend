# ============ FIRST STAGE
FROM maven:3.9.5-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ============ SECOND STAGE
FROM eclipse-temurin:17-jre
WORKDIR /app
ARG APP_NAME=RetinaCareBackend
COPY --from=builder /app/target/${APP_NAME}.jar /app/${APP_NAME}.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/${APP_NAME}.jar"]