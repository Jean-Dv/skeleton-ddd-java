package dev.jean.shared.domain;

import java.util.Random;

/**
 * This class is a factory for picker random elements from
 * an array or list.
 */
public final class RandomElementPicker {
    /**
     * Pick a random element from an array.
     * 
     * @param <T>      The type of the elements in the array.
     * @param elements The array of elements.
     * @return A random element from the array.
     */
    @SafeVarargs
    public static <T> T from(T... elements) {
        Random random = new Random();

        return elements[random.nextInt(elements.length)];
    }
}
