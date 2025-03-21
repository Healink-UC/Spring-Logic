// src\main\java\com\plexus\integrator\HealthController.java
package com.healink.integrador;

import java.util.Map;
import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/healthz")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/api/test")
    public ResponseEntity<Object> test() {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "API is working correctly",
                "timestamp", new Date()));
    }

}