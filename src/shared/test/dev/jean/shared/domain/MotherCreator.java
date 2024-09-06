package dev.jean.shared.domain;

import com.github.javafaker.Faker;

/**
 * This class is used to create random values for the tests.
 * 
 * @see Faker
 */
public class MotherCreator {
    private final static Faker faker = new Faker();

    /**
     * This method returns a class to generate
     * random values.
     * 
     * @return Faker class
     */
    public static Faker random() {
        return faker;
    }
}
