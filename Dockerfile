# Stage 1: Build the JAR with Maven & JDK 21
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and package
COPY src ./src
RUN mvn package -DskipTests -B

# Stage 2: Lightweight JRE 21 Runtime Image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root application user for container security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup && mkdir -p /app/uploads && chown -R appuser:appgroup /app

USER appuser

# Copy the fat JAR from builder
COPY --from=builder /app/target/swachh-sathi-1.0.0.jar app.jar

# Dynamic port for cloud environments (Render/Railway inject $PORT)
ENV PORT=8080
EXPOSE 8080

# Production container JVM flags
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
