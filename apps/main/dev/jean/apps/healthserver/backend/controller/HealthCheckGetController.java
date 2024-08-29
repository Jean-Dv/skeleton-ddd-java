package dev.jean.apps.healthserver.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HealthCheckGetController {

    @GetMapping("/health-check")
    public HashMap<String, String> index() {
        HashMap<String, String> status = new HashMap<>();
        status.put("application", "healthserver_backend");
        status.put("status", "ok");

        return status;
    }

}
