package dev.jean.shared.infrastructure.bus.event.rabbitmq;

/**
 * This class is responsible for formatting the exchange names for RabbitMQ.
 * It is used to format the exchange names the retry exchange, and the dead
 * letter exchange.
 */
public final class RabbitMqExchangeNameFormatter {
    /**
     * This method formats the exchange name for the retry exchange.
     * It appends the string "retry-" to the beginning of the exchange name.
     * 
     * @param exchangeName The exchange name.
     * @return The formatted exchange name.
     */
    public static String retry(String exchangeName) {
        return String.format("retry-%s", exchangeName);
    }

    /**
     * This method formats the exchange name for the dead letter exchange.
     * It appends the string "dead_letter-" to the beginning of the exchange name.
     * 
     * @param exchangeName The exchange name.
     * @return The formatted exchange name.
     */
    public static String deadLetter(String exchangeName) {
        return String.format("dead_letter-%s", exchangeName);
    }
}
