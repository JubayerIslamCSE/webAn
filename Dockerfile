# ─────────────────────────────────────────────
# Stage 1: Build the JAR using Maven + Java 25
# ─────────────────────────────────────────────
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

# Copy Maven wrapper and pom first (layer caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies only (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy all source code
COPY src src

# Build the JAR, skip tests
RUN ./mvnw package -DskipTests -B

# ─────────────────────────────────────────────
# Stage 2: Run the JAR with slim JRE
# ─────────────────────────────────────────────
FROM eclipse-temurin:25-jre

WORKDIR /app

# Create uploads directory inside container
RUN mkdir -p uploads

# Copy the built JAR from Stage 1
COPY --from=builder /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]