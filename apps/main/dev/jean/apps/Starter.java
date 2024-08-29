package dev.jean.apps;

import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;

import dev.jean.apps.healthserver.backend.HealthServerApplication;

public class Starter {
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Usage: java -jar starter.jar <app> <args>");
        }

        String applicationName = args[0];
        String commandName = args[1];
        boolean isServerCommand = commandName.equals("server");

        Class<?> applicationClass = applications().get(applicationName);

        SpringApplication app = new SpringApplication(applicationClass);

        if (!isServerCommand) {
            app.setWebApplicationType(WebApplicationType.NONE);
        }

        app.run(args);
    }

    private static HashMap<String, Class<?>> applications() {
        HashMap<String, Class<?>> applications = new HashMap<>();

        applications.put("healthserver_backend", HealthServerApplication.class);
        return applications;
    }
}
