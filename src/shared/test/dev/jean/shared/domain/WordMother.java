package dev.jean.shared.domain;

/**
 * This class is a factory for Word objects.
 */
public final class WordMother {
    /**
     * Create a random word.
     * 
     * @return A random word.
     */
    public static String random() {
        return MotherCreator.random().lorem().word();
    }
}
