# ============ FIRST STAGE
FROM maven:3.9.5-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ============ SECOND STAGE
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/RetinaCareBackend.jar /app/RetinaCareBackend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/RetinaCareBackend.jar"]