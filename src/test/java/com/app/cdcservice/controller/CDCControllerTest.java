package com.app.cdcservice.controller;

import com.app.cdcservice.service.CDCService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CDCControllerTest {

    @Mock
    private CDCService cdcService;

    @InjectMocks
    private CDCController cdcController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStartStreaming() {
        String expectedResponse = "CDC streaming started successfully.";
        when(cdcService.startStreaming()).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        ResponseEntity<String> response = cdcController.startStreaming();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testStopStreaming() {
        String expectedResponse = "CDC streaming stopped successfully.";
        when(cdcService.stopStreaming()).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        ResponseEntity<String> response = cdcController.stopStreaming();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testGetStreamingStatus() {
        String expectedResponse = "RUNNING";
        when(cdcService.getStreamingStatus()).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        ResponseEntity<String> response = cdcController.getStreamingStatus();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testInsertData() {
        String requestData = "{\"name\": \"John Doe\", \"email\": \"john@example.com\"}";
        String expectedResponse = "Data inserted successfully.";
        when(cdcService.insertData(requestData)).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        ResponseEntity<String> response = cdcController.insertData(requestData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testRetrieveData() {
        String expectedResponse = "[{\"name\": \"John Doe\", \"email\": \"john@example.com\"}]";
        when(cdcService.retrieveData()).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        ResponseEntity<String> response = cdcController.retrieveData();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}
