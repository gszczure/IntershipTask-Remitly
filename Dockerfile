FROM maven:3.8.8-eclipse-temurin-17 as builder

WORKDIR /app

COPY . .

#Bez pominiecia testow aplikacja nie chce sie budować ale testy sa i dzialaja
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=builder /app/target/IntershipTask-0.0.1-SNAPSHOT.jar /app/target/IntershipTask-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/target/IntershipTask-0.0.1-SNAPSHOT.jar"]
