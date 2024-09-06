package dev.jean.shared.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Configuration to load the environment file.
 * 
 * @see Configuration
 */
@Configuration
public class EnviromentConfig {
    ResourceLoader resourceLoader;

    public EnviromentConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Load the environment file.
     * 
     * @return The class to get parameters from the environment file.
     */
    @Bean
    protected Dotenv dotenv() {
        Resource resource = resourceLoader.getResource("classpath:/.env.local");

        return Dotenv
                .configure()
                .directory("/")
                .filename(resource.exists() ? ".env.local" : ".env")
                .load();
    }
}
