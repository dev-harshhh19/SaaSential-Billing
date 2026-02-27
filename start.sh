#!/bin/bash

echo "Starting SaaSential Billing Management System..."
echo "==============================================="

# Change to the backend directory
cd backend || { echo "Backend directory not found"; exit 1; }

# Make maven wrapper executable just in case
chmod +x mvnw

# Run the Spring Boot application
echo "Building and starting the Spring Boot application..."
./mvnw spring-boot:run
