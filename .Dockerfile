# Use a lightweight OpenJDK image
FROM openjdk:21-jdk

# Set working directory
WORKDIR /app

# Copy build artifacts
COPY build/libs/ticket-system-1.0.0.jar app.jar

# Expose app port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
