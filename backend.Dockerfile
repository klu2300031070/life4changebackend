# Stage 1: Build the app
FROM eclipse-temurin:24-jdk AS builder

WORKDIR /app

COPY mvnw .          
COPY .mvn/ .mvn
COPY pom.xml ./
COPY src ./src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:24-jdk

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]