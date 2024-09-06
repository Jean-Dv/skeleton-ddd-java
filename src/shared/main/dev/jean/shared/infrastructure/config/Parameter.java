package dev.jean.shared.infrastructure.config;

import dev.jean.shared.domain.Service;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Service to get parameters from the environment file.
 */
@Service
public final class Parameter {
    private final Dotenv dotenv;

    public Parameter(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    /**
     * Get a parameter from the environment file.
     * 
     * @param key The key of the parameter.
     * @return The value of the parameter.
     * @throws ParameterNotExist
     */
    public String get(String key) throws ParameterNotExist {
        String value = dotenv.get(key);

        if (value == null) {
            throw new ParameterNotExist(key);
        }

        return value;
    }

    /**
     * Get a parameter from the environment file
     * and parse it to an integer.
     * 
     * @param key The key of the parameter.
     * @return The value of the parameter.
     * @throws ParameterNotExist
     */
    public Integer getInt(String key) throws ParameterNotExist {
        String value = get(key);

        return Integer.parseInt(value);
    }
}
