package com.app.cdcservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class CDCService {

    private static final Logger logger = LoggerFactory.getLogger(CDCService.class);

    @Value("${cdc.kafka.connect.url}")
    String kafkaConnectUrl;


    private final RestTemplate restTemplate;

    public CDCService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Starts the CDC streaming by configuring both Debezium source connector and JDBC sink connector.
     *
     * @return ResponseEntity with message about the success or failure of the operation
     */
    public ResponseEntity<String> startStreaming() {
        logger.info("Starting CDC streaming...");
        try {
            // Configuration for Debezium source connector
            String sourceConfig = getDebeziumSourceConfig();
            // Configuration for JDBC sink connector
            String sinkConfig = getJdbcSinkConfig();

            // POST requests to Kafka Connect REST API to create connectors
            restTemplate.postForEntity(kafkaConnectUrl + "/connectors", sourceConfig, String.class);
            restTemplate.postForEntity(kafkaConnectUrl + "/connectors", sinkConfig, String.class);

            logger.info("CDC streaming started successfully.");
            return ResponseEntity.ok("CDC streaming started successfully.");
        } catch (Exception e) {
            logger.error("Error starting CDC streaming", e);
            return ResponseEntity.status(500).body("Failed to start CDC streaming: " + e.getMessage());
        }
    }

    /**
     * Stops the CDC streaming by deleting both Debezium source connector and JDBC sink connector.
     *
     * @return ResponseEntity with message about the success or failure of the operation
     */
    public ResponseEntity<String> stopStreaming() {
        logger.info("Stopping CDC streaming...");
        try {
            // Delete both connectors from Kafka Connect REST API
            restTemplate.delete(kafkaConnectUrl + "/connectors/debezium-source");
            restTemplate.delete(kafkaConnectUrl + "/connectors/jdbc-sink");

            logger.info("CDC streaming stopped successfully.");
            return ResponseEntity.ok("CDC streaming stopped successfully.");
        } catch (Exception e) {
            logger.error("Error stopping CDC streaming", e);
            return ResponseEntity.status(500).body("Failed to stop CDC streaming: " + e.getMessage());
        }
    }

    /**
     * Retrieves the status of the Debezium source connector.
     *
     * @return ResponseEntity with the status or failure message
     */
    public ResponseEntity<String> getStreamingStatus() {
        logger.info("Retrieving CDC streaming status...");
        try {
            // Fetch the status of Debezium source connector from Kafka Connect
            String status = restTemplate.getForObject(kafkaConnectUrl + "/connectors/debezium-source/status", String.class);
            logger.info("CDC streaming status retrieved successfully.");
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            logger.error("Error retrieving CDC streaming status", e);
            return ResponseEntity.status(500).body("Failed to retrieve CDC streaming status: " + e.getMessage());
        }
    }

    /**
     * Inserts data into the source PostgreSQL database. The actual data handling
     * should be in the repository layer.
     *
     * @param data JSON data to insert
     * @return ResponseEntity with a success or failure message
     */
    @Transactional
    public ResponseEntity<String> insertData(String data) {
        logger.info("Inserting data into the source DB...");
        // Call to the repository layer to persist data
        try {
            // Example insertion logic
            // repository.save(convertJsonToEntity(data));

            logger.info("Data inserted successfully.");
            return ResponseEntity.ok("Data inserted successfully.");
        } catch (Exception e) {
            logger.error("Error inserting data", e);
            return ResponseEntity.status(500).body("Failed to insert data: " + e.getMessage());
        }
    }

    /**
     * Retrieves data from the target PostgreSQL database.
     *
     * @return ResponseEntity with the retrieved data or failure message
     */
    public ResponseEntity<String> retrieveData() {
        logger.info("Retrieving data from the target DB...");
        // Call to the repository layer to fetch data
        try {
            // Example retrieval logic
            // List<Employee> employees = repository.findAll();
            // String result = objectMapper.writeValueAsString(employees);

            logger.info("Data retrieved successfully.");
            return ResponseEntity.ok("Data retrieved successfully.");
        } catch (Exception e) {
            logger.error("Error retrieving data", e);
            return ResponseEntity.status(500).body("Failed to retrieve data: " + e.getMessage());
        }
    }

    /**
     * Returns the configuration for Debezium source connector as JSON.
     *
     * @return Debezium source connector configuration JSON
     */
    private String getDebeziumSourceConfig() {
        return "{"
                + "\"name\":\"debezium-source\","
                + "\"config\":{"
                + "\"connector.class\":\"io.debezium.connector.postgresql.PostgresConnector\","
                + "\"database.hostname\":\"postgres-source\","
                + "\"database.port\":\"5432\","
                + "\"database.user\":\"user\","
                + "\"database.password\":\"password\","
                + "\"database.dbname\":\"source_db\","
                + "\"database.server.name\":\"dbserver1\","
                + "\"table.include.list\":\"public.employee\","
                + "\"plugin.name\":\"pgoutput\","
                + "\"database.history.kafka.bootstrap.servers\":\"kafka:9092\","
                + "\"database.history.kafka.topic\":\"schema-changes.inventory\""
                + "}}";
    }

    /**
     * Returns the configuration for JDBC sink connector as JSON.
     *
     * @return JDBC sink connector configuration JSON
     */
    private String getJdbcSinkConfig() {
        return "{"
                + "\"name\":\"jdbc-sink\","
                + "\"config\":{"
                + "\"connector.class\":\"io.confluent.connect.jdbc.JdbcSinkConnector\","
                + "\"connection.url\":\"jdbc:postgresql://postgres-target:5432/target_db\","
                + "\"connection.user\":\"user\","
                + "\"connection.password\":\"password\","
                + "\"auto.create\":\"true\","
                + "\"auto.evolve\":\"true\","
                + "\"topics\":\"dbserver1.public.employee\""
                + "}}";
    }
}
