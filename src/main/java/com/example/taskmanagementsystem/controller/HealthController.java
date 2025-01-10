package com.example.taskmanagementsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping(value = "/health")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> adminHealthCheck() {
        return ResponseEntity.ok("Ok Report");
    }

    @GetMapping(value = "/")
    public ResponseEntity<Object> healthCheck() {
        return ResponseEntity.ok("Ok Report");
    }
}
