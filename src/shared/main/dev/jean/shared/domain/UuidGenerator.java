package dev.jean.shared.domain;

/**
 * This interface represents a UUID generator.
 */
public interface UuidGenerator {
    /**
     * Generates a UUID.
     * 
     * @return The generated UUID.
     */
    String generate();
}
