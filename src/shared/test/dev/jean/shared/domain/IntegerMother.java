package dev.jean.shared.domain;

/**
 * This class is used to create random integer
 * values for the tests.
 * 
 * @see MotherCreator
 */
public final class IntegerMother {
    /**
     * This method returns a random integer.
     * 
     * @return Random integer
     */
    public static Integer random() {
        return MotherCreator.random().number().randomDigit();
    }
}
