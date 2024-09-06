package dev.jean.shared.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;

/**
 * Utility class for shared domain operations.
 */
public final class Utils {
    /**
     * Converts a LocalDateTime to a string.
     * 
     * @param dateTime LocalDateTime to convert.
     * @return Representation in string of the LocalDateTime.
     */
    public static String dateToString(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Converts a Timestamp to a string.
     * 
     * @param timestamp Timestamp to convert.
     * @return Representation in string of the Timestamp.
     */
    public static String dateToString(Timestamp timestamp) {
        return dateToString(timestamp.toLocalDateTime());
    }

    /**
     * Encodes a HashMap to a JSON string.
     * 
     * @param map HashMap to encode.
     * @return JSON string representation of the HashMap.
     */
    public static String jsonEncode(HashMap<String, Serializable> map) {
        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    /**
     * Decodes a JSON string to a HashMap.
     * 
     * @param json JSON string to decode.
     * @return HashMap representation of the JSON string.
     */
    public static HashMap<String, Serializable> jsonDecode(String body) {
        try {
            return new ObjectMapper().readValue(body,
                    new TypeReference<HashMap<String, Serializable>>() {
                    });
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Converts a string to snake case.
     * 
     * @param text String to convert.
     * @return String in snake case.
     */
    public static String toSnake(String text) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, text);
    }

    /**
     * Converts a string to camel case.
     * 
     * @param text String to convert.
     * @return String in camel case.
     */
    public static String toCamel(String text) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, text);
    }

    /**
     * Converts a string to camel case with the first letter in lower case.
     * 
     * @param text String to convert.
     * @return String in camel case with the first letter in lower case.
     */
    public static String toCamelFirstLower(String text) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, text);
    }

    /**
     * Retries an operation a number of times with a wait time between retries.
     * 
     * @param numberOfRetries  Number of retries.
     * @param waitTimeInMillis Wait time between retries.
     * @param operation        Operation to retry.
     * @throws Exception If the operation fails after all retries.
     */
    public static void retry(int numberOfRetries, long waitTimeInMillis, Runnable operation) throws Exception {
        for (int i = 0; i < numberOfRetries; i++) {
            try {
                operation.run();
                return; // Success, exit the method
            } catch (Exception e) {
                System.out.println("Retry " + (i + 1) + "/" + numberOfRetries + " fail. Retrying...");
                if (i >= numberOfRetries - 1) {
                    throw e;
                }

                try {
                    Thread.sleep(waitTimeInMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();

                    throw new Exception("Operation interrupted while retrying", ie);
                }
            }
        }
    }
}
