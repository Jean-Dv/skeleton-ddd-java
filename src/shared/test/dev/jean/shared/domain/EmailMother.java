package dev.jean.shared.domain;

/**
 * This class is a factory for Email objects.
 */
public final class EmailMother {
    /**
     * Create a random email.
     * 
     * @return A random email.
     */
    public static String random() {
        return MotherCreator.random().internet().emailAddress();
    }
}
