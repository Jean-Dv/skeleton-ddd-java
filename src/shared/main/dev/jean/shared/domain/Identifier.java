package dev.jean.shared.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * This class represents an identifier.
 */
public abstract class Identifier implements Serializable {
    final protected String value;

    public Identifier(String value) {
        this.ensureValidUuid(value);
        this.value = value;
    }

    protected Identifier() {
        this.value = null;
    }

    public String value() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Identifier that = (Identifier) o;
        return value.equals(that.value);
    }

    /**
     * Ensures the value is a valid UUID.
     * 
     * @param value The value to check.
     * @throws IllegalArgumentException
     */
    private void ensureValidUuid(String value) throws IllegalArgumentException {
        UUID.fromString(value);
    }
}
