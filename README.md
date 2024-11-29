# Employee CRUD Application - Spring Boot

## Project Overview

This is a **Spring Boot CRUD** application that allows you to manage employee records. The application connects to a **PostgreSQL** database and supports basic CRUD operations (Create, Read, Update, Delete). It is designed to be modular,
clean, and easy to deploy with a full CI/CD pipeline using **GitHub Actions** that deploys the application to an **AWS
EC2** instance.

The project is fully integrated with modern tools such as **Docker** for containerization, **JUnit** for unit and
integration tests, and **GitHub Actions** for continuous integration and deployment.

## Technologies Used

- **Spring Boot**: A framework for building Java-based applications.
- **PostgreSQL**: A relational database used for storing employee records.
- **Docker**: Containerization tool used for PostgreSQL database and application setup.
- **JUnit**: Framework for unit and integration testing.
- **GitHub Actions**: CI/CD pipeline for automating tests, builds, and deployments.
- **AWS EC2**: Cloud platform used for hosting and deploying the application.

## How to Run the Project Locally

### Prerequisites

Make sure you have the following installed:

- **Java 17+**
- **Maven**
- **Docker** (for running PostgreSQL locally)

### Steps to Run Locally

1. **Clone the repository**:

    ```bash
    git clone https://github.com/ahmed-baz/employee-service.git
    ```

2. **Navigate to the project folder**:

    ```bash
    cd employee-service
    ```

3. **Set up the PostgreSQL database** using Docker:

   If you're using Docker to run PostgreSQL, the repository includes a `docker-compose.yml` file to spin up the
   database:

    ```bash
    docker-compose up -d
    ```

   This will start the PostgreSQL container in the background.

4. **Build the application**:

   Run the following command to build the project using Maven:

    ```bash
    ./mvnw clean install
    ```

5. **Run the application**:

   To start the application, use the following command:

    ```bash
    ./mvnw spring-boot:run
    ```

6. **Access the application**:

   Open your browser and visit `http://localhost:8080` to interact with the Employee CRUD application.
