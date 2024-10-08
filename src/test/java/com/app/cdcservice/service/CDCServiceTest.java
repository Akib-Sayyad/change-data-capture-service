package com.app.cdcservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class CDCServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CDCService cdcService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize the value of kafkaConnectUrl
        cdcService.kafkaConnectUrl = "http://localhost:8083";
    }

    @Test
    public void testStartStreamingSuccess() {
        ResponseEntity<String> successResponse = new ResponseEntity<>("Connector created", HttpStatus.OK);

        // Mock the RestTemplate to simulate a successful response for both connectors
        when(restTemplate.postForEntity(anyString(), any(String.class), any(Class.class)))
                .thenReturn(successResponse);

        // Act
        ResponseEntity<String> response = cdcService.startStreaming();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("CDC streaming started successfully.", response.getBody());
    }

    @Test
    public void testStartStreamingFailure() {
        // Arrange
        doThrow(new RuntimeException("Kafka Connect is down")).when(restTemplate).postForEntity(anyString(), anyString(), any());

        // Act
        ResponseEntity<String> response = cdcService.startStreaming();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to start CDC streaming: Kafka Connect is down", response.getBody());
    }

    @Test
    public void testStopStreamingSuccess() {
        // Act
        ResponseEntity<String> response = cdcService.stopStreaming();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("CDC streaming stopped successfully.", response.getBody());
    }

    @Test
    public void testStopStreamingFailure() {
        // Arrange
        doThrow(new RuntimeException("Failed to stop connectors")).when(restTemplate).delete(anyString());

        // Act
        ResponseEntity<String> response = cdcService.stopStreaming();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to stop CDC streaming: Failed to stop connectors", response.getBody());
    }

    @Test
    public void testGetStreamingStatusSuccess() {
        // Arrange
        String mockStatusResponse = "{ \"status\": \"RUNNING\" }";
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockStatusResponse);

        // Act
        ResponseEntity<String> response = cdcService.getStreamingStatus();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockStatusResponse, response.getBody());
    }

    @Test
    public void testGetStreamingStatusFailure() {
        // Arrange
        doThrow(new RuntimeException("Failed to get status")).when(restTemplate).getForObject(anyString(), any());

        // Act
        ResponseEntity<String> response = cdcService.getStreamingStatus();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to retrieve CDC streaming status: Failed to get status", response.getBody());
    }

    @Test
    public void testInsertDataSuccess() {
        // Act
        ResponseEntity<String> response = cdcService.insertData("{ \"name\": \"John\" }");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Data inserted successfully.", response.getBody());
    }
}
