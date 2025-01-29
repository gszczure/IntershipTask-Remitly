FROM maven:3.8.8-eclipse-temurin-17 as builder

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=builder /app /app

EXPOSE 8080

CMD ["java", "-jar", "/app/target/IntershipTask-0.0.1-SNAPSHOT.jar"]
