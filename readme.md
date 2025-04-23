News Analytics Application
=========================

This project is a microservices-based application built using **Java**, **Spring Boot**, and **Maven**. It includes multiple services such as `elastic-query-service`, `kafka-stream-service`, and `analytics-service`. The application integrates with **Kafka**, **PostgreSQL**, and **Thymeleaf** for various functionalities.

Table of Contents
-----------------

*   [Features](#features)
*   [Technologies Used](#technologies-used)
*   [Prerequisites](#prerequisites)
*   [Setup and Installation](#setup-and-installation)
*   [Configuration](#configuration)
*   [Running the Application](#running-the-application)
*   [Endpoints](#endpoints)
*   [License](#license)

Features
--------

*   **Elastic Query Service**: Provides search functionality using Elasticsearch.
*   **Kafka Integration**: Streams data between services using Kafka topics.
*   **Analytics Service**: Processes and analyzes data from Kafka streams.
*   **Thymeleaf Integration**: Provides a web interface for user interaction.
*   **OAuth2 Security**: Secures endpoints using JWT-based authentication.

Technologies Used
-----------------

*   Java 17
*   Spring Boot
*   Spring Security (OAuth2)
*   Apache Kafka
*   PostgreSQL
*   Thymeleaf
*   Maven

Prerequisites
-------------

Before running the application, ensure you have the following installed:

*   Java 17
*   Maven
*   Docker (for running Kafka and PostgreSQL)
*   Elasticsearch

Setup and Installation
----------------------

1.  Clone the repository:

        git clone https://github.com/your-repo/microservices-application.git
        cd microservices-application

2.  Build the project:

        mvn clean install

3.  Start the required services (Kafka, PostgreSQL, Elasticsearch) using Docker:

        docker-compose up -d

4.  Configure the application properties for each service in the `config-server-repository` directory.

Configuration
-------------

### Kafka Configuration

    kafka-config:
      bootstrap-servers: localhost:19092, localhost:29092, localhost:39092
      schema-registry-url: http://localhost:8190

### PostgreSQL Configuration

    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/postgres?currentSchema=analytics
        username: admin
        password: 123

### OAuth2 Configuration

    spring:
      security:
        oauth2:
          resourceserver:
            jwt:
              issuer-uri: http://localhost:8189/auth/realms/microservices_realm

Running the Application
-----------------------

1.  Start the Config Server:

        cd config-server
        mvn spring-boot:run

2.  Start the individual microservices:

        cd elastic-query-service
        mvn spring-boot:run

        cd kafka-stream-service
        mvn spring-boot:run

        cd analytics-service
        mvn spring-boot:run

3.  Access the application:
    *   **Elastic Query Service**: [http://localhost:8183/elastic-query-web-client](http://localhost:8183/elastic-query-web-client)
    *   **Analytics Service**: [http://localhost:8185/analytics](http://localhost:8185/analytics)

Endpoints
---------

### Elastic Query Service

*   `GET /elastic-query-service/v1/search`: Search for data in Elasticsearch.

### Analytics Service

*   `GET /analytics/v1/data`: Retrieve analytics data.

### Swagger UI

Access API documentation at `/swagger-ui.html`.

License
-------

This project is licensed under the MIT License. See the `LICENSE` file for details.