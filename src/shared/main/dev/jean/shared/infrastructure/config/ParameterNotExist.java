package dev.jean.shared.infrastructure.config;

/**
 * Exception to be thrown when a parameter does not exist in the environment
 * file.
 */
public final class ParameterNotExist extends Throwable {
    public ParameterNotExist(String key) {
        super(String.format("The parameter <%s> does not exist in the environment file", key));
    }
}
