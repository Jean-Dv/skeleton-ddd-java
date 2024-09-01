package dev.jean.shared.domain;

import java.util.Objects;

/**
 * This class represents an integer value object.
 */
public class IntValueObject {
    private Integer value;

    public IntValueObject(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        IntValueObject that = (IntValueObject) o;
        return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
