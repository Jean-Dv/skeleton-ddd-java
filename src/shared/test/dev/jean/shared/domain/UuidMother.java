package dev.jean.shared.domain;

import java.util.UUID;

/**
 * This class is a factory for UUID objects.
 */
public final class UuidMother {
    /**
     * Create a random UUID.
     * 
     * @return A random UUID.
     */
    public static String random() {
        return UUID.randomUUID().toString();
    }
}
