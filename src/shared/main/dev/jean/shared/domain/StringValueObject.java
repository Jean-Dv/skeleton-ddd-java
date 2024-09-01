package dev.jean.shared.domain;

import java.util.Objects;

/**
 * This class represents a string value object.
 */
public abstract class StringValueObject {
    private String value;

    public StringValueObject(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return this.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof StringValueObject))
            return false;
        StringValueObject that = (StringValueObject) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

}
