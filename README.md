# Outdoor Equipment Rent API

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technologies Used](#technologies-used)
3. [Requirements](#requirements)
4. [Installation](#installation)
5. [Configuration](#configuration)
6. [Running the Application](#running-the-application)
7. [API Endpoints](#api-endpoints)
8. [Testing](#testing)
9. [Contributors](#contributors)
10. [Future Development](#future-development)

## Project Overview

The **Outdoor Equipment Rent API** is a backend service developed using **Java**, **Spring Boot**, and **Hibernate** to support an equipment rental platform. It provides functionality to manage outdoor equipment, renters, and rentals, as well as fetch weather forecasts for specific locations. The API can be integrated with frontend applications to streamline the rental process and offer additional features for managing equipment availability and rental logistics.

### Features:
- **Equipment Management**: Manage outdoor equipment inventory with create, read, and delete operations.
- **Renter Management**: Handle renter data, including create, update, and retrieve operations.
- **Rental Management**: Create, update, and track equipment rentals.
- **Weather Forecast**: Fetch real-time weather information for any location to assist renters in planning their outdoor activities.

## Technologies Used
- **Java 17**
- **Spring Boot 3.x**
- **Hibernate** as ORM
- **MySQL** as the database
- **Gradle** for build and dependency management
- **Lombok** to reduce boilerplate code
- **JUnit 5** for unit testing
- **Swagger** for API documentation

## Requirements
Before running the application, ensure you have the following installed:
- **Java 17** or higher
- **Gradle 7.x** or higher
- **MySQL** database
- **Postman** or a similar tool to test the API

## Installation

1. Clone the repository:
```
   https://github.com/martkow/outdoor-equip-rent.git
```
2. Build the project with Gradle:
```
   ./gradlew build
```
3. Ensure you have a running database and create a new database for the application:
```
   CREATE DATABASE outdoor_rent_db;
```

## Configuration

Configure the database and application properties in:

`src/main/resources/application.properties`:

```
# MySQL configuration
spring.datasource.url=jdbc:mysql://localhost:3306/outdoor_equip_rent?serverTimezone=Europe/Warsaw&allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
# Hibernate Naming Strategy
spring.jpa.hibernate.naming.physical-strategy = org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

### Running the Application

You can run the application using Gradle.
The application will be available at http://localhost:8080 by default.
Swagger documentation will be available at http://localhost:8080/swagger-ui/index.html#/.

### API Endpoints

**Equipment Management:**
- POST /api/equipment/categories/{category}/equipment – Add equipment to a specific category.
- GET /api/equipment – Get a list of all available equipment, with optional filtering by category.
- GET /api/equipment/categories – Retrieve a list of all available equipment categories.
- DELETE /api/equipment/categories/{category}/equipment/{equipmentId} – Delete equipment by its ID from a specific category.

**Weather Forecast:**
- GET /api/weather – Fetch the weather forecast for any location.

**Rental Management:**
- GET /api/rentals – Retrieve all rentals.
- POST /api/rentals – Create a new rental.
- PATCH /api/rentals/{rentalId}/status – Update the status of a rental by its ID.

**Renter Management:**
- GET /api/renters/{renterId} – Get renter details by ID.
- PUT /api/renters/{renterId} – Update an existing renter’s details.
- GET /api/renters – Get a list of all renters.
- POST /api/renters – Create a new renter.
- GET /api/renters/{renterId}/rentals – Retrieve all rentals made by a specific renter.

## Testing
To run tests for the project, use the following command:
```
  ./gradlew test
```

## Contributors
- Marta Kowalczyk - [GitHub](https://github.com/martkow)

## Future development
The Outdoor Equipment Rent API has been designed with scalability in mind, and future improvements may include:
- Integration with third-party payment services.
- Advanced filtering options for equipment availability.
- Real-time tracking of rental equipment and availability updates.

This `README.md` provides a complete overview of the API, with information on installation, configuration, and usage, designed for a project that uses **Gradle** as the build tool. You can adjust the file to reflect the specific details of your project as necessary.


