# CDC Service with Spring Boot, Kafka, and Debezium

## Overview
This project implements a Change Data Capture (CDC) service that streams data from a PostgreSQL source database to a target database using Kafka and Debezium.

## Requirements
- Docker
- Maven
- Java 11+

## Setup
1. Clone this repository.
2. Navigate to the project directory.
3. Run `docker-compose up` to start the services.

## Endpoints
- **Start Streaming**: `POST /api/streaming/start`
- **Stop Streaming**: `POST /api/streaming/stop`
- **Get Streaming Status**: `GET /api/streaming/status`
- **Data Insertion**: `POST /api/streaming/insert` (JSON body)
- **Retrieve Data**: `GET /api/streaming/retrieve`

## Running Tests
Run tests using Maven:

```bash
mvn test
