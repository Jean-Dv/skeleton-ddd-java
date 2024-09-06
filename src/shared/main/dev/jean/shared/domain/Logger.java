package dev.jean.shared.domain;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This interface represents a logger.
 */
public interface Logger {
    /**
     * Logs an info message.
     * 
     * @param $message The message to log.
     */
    void info(String $message);

    /**
     * Logs an info message.
     *
     * @param $message The message to log.
     * @param $context The context to log.
     */
    void info(String $message, HashMap<String, Serializable> $context);

    /**
     * Logs a warning message.
     * 
     * @param $message The message to log.
     */
    void warning(String $message);

    /**
     * Logs a warning message.
     *
     * @param $message The message to log.
     * @param $context The context to log.
     */
    void warning(String $message, HashMap<String, Serializable> $context);

    /**
     * Logs an error message.
     * 
     * @param $message The message to log.
     */
    void critical(String $message);

    /**
     * Logs an error message.
     *
     * @param $message The message to log.
     * @param $context The context to log.
     */
    void critical(String $message, HashMap<String, Serializable> $context);
}
