package dev.jean.shared.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * This class is a factory for List objects.
 */
public final class ListMother {
    /**
     * Create a list of size elements.
     * 
     * @param <T>     The type of the elements in the list.
     * @param size    The size of the list.
     * @param creator A function that creates an element of type T.
     * @return A list of size elements.
     */
    public static <T> List<T> create(Integer size, Supplier<T> creator) {
        ArrayList<T> list = new ArrayList<T>();

        for (int i = 0; i < size; i++) {
            list.add(creator.get());
        }

        return list;
    }

    /**
     * Create a list of random size elements.
     * 
     * @param <T>     The type of the elements in the list.
     * @param creator A function that creates an element of type T.
     * @return A list of random size elements.
     */
    public static <T> List<T> random(Supplier<T> creator) {
        return create(IntegerMother.random(), creator);
    }

    /**
     * Create a list of one element.
     * 
     * @param <T>     The type of the element.
     * @param element The element.
     * @return A list of one element.
     */
    public static <T> List<T> one(T element) {
        return Collections.singletonList(element);
    }
}
