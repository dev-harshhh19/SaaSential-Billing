# SaaSential Billing

A robust, minimal SaaS billing and subscription management application built with Java, Spring Boot, and Thymeleaf.

## Features

- **Authentication**: JWT-based user login and registration.
- **Subscription Plans**: CRUD plan management with different tiers and billing cycles.
- **Subscription Engine**: Manage user active plans, pause, upgrade, and downgrade flows.
- **Usage Tracking**: Record metric events against subscriptions for metered billing.
- **Invoicing**: Automatic chronological invoice generation with overage bounds.
- **Dashboard**: Simple, responsive frontend interface powered by Thymeleaf and Tailwind CSS.
- **Embedded Database**: H2 local file mode for frictionless data persistence zero-setup required.

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Security 
- Spring Data JPA
- H2 Database
- HTML / CSS / JavaScript
- Tailwind CSS via CDN

## Setup & Running

A seamless quick-start interface is provided out of the box via the root-level bash/batch scripts. 

### Prerequisites

- Java 17 or higher
- Maven (Embedded via Maven Wrapper)

### Quick Start

On Windows:
```cmd
./start.bat
```

On Mac/Linux:
```bash
./start.sh
```

This will cleanly wrap, compile, and launch the Spring Boot backend server on `http://localhost:8080`. 

## Usage

After starting the application, navigate to:
1. `http://localhost:8080/` to test login and registration logic.
2. `http://localhost:8080/dashboard` to manage user subscriptions.
3. `http://localhost:8080/admin` to observe system metrics.

Database: The data is saved safely in the `./backend/saasential_db.mv.db` local file.
