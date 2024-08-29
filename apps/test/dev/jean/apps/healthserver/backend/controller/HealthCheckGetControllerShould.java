package dev.jean.apps.healthserver.backend.controller;

import org.junit.jupiter.api.Test;

import dev.jean.apps.ApplicationTestCase;

public class HealthCheckGetControllerShould extends ApplicationTestCase {
    @Test
    public void check_the_app_is_working_ok() throws Exception {
        this.assertResponse("/health-check", 200, "{'application': 'healthserver_backend', 'status': 'ok'}");
    }

}
