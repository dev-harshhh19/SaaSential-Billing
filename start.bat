@echo off
echo Starting SaaSential Billing Management System...
echo ===============================================

:: Change to the backend directory
cd backend

:: Run the Spring Boot application using the Maven wrapper
echo Building and starting the Spring Boot application...
call .\mvnw spring-boot:run

pause
