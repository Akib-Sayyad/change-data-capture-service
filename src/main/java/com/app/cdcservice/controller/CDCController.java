package com.app.cdcservice.controller;

import com.app.cdcservice.service.CDCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streaming")
public class CDCController {

    private static final Logger logger = LoggerFactory.getLogger(CDCController.class);
    private final CDCService cdcService;

    public CDCController(CDCService cdcService) {
        this.cdcService = cdcService;
    }

    /**
     * Starts the CDC streaming service.
     *
     * @return ResponseEntity with the success or failure message
     */
    @PostMapping("/start")
    public ResponseEntity<String> startStreaming() {
        logger.info("Received request to start streaming.");
        return cdcService.startStreaming();
    }

    /**
     * Stops the CDC streaming service.
     *
     * @return ResponseEntity with the success or failure message
     */
    @PostMapping("/stop")
    public ResponseEntity<String> stopStreaming() {
        logger.info("Received request to stop streaming.");
        return cdcService.stopStreaming();
    }

    /**
     * Retrieves the status of the CDC streaming service.
     *
     * @return ResponseEntity with the status or failure message
     */
    @GetMapping("/status")
    public ResponseEntity<String> getStreamingStatus() {
        logger.info("Received request to get streaming status.");
        return cdcService.getStreamingStatus();
    }

    /**
     * Inserts data into the source PostgreSQL database.
     *
     * @param data JSON data to insert
     * @return ResponseEntity with the success or failure message
     */
    @PostMapping("/insert")
    public ResponseEntity<String> insertData(@RequestBody String data) {
        logger.info("Received request to insert data.");
        return cdcService.insertData(data);
    }

    /**
     * Retrieves data from the target PostgreSQL database.
     *
     * @return ResponseEntity with the retrieved data or failure message
     */
    @GetMapping("/retrieve")
    public ResponseEntity<String> retrieveData() {
        logger.info("Received request to retrieve data.");
        return cdcService.retrieveData();
    }
}