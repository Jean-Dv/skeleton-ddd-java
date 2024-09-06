package dev.jean.shared.infrastructure.bus.event.rabbitmq;

import dev.jean.shared.infrastructure.bus.event.DomainEventSubscriberInformation;

/**
 * This class is responsible for formatting the queue names for RabbitMQ.
 * It is used to format the queue names for the main queue, the retry queue,
 * and the dead letter queue.
 * 
 * @see DomainEventSubscriberInformation
 */
public final class RabbitMqQueueNameFormatter {
    /**
     * This method formats the queue name for the main queue.
     *
     * @param information The DomainEventSubscriberInformation object.
     * @return The formatted queue name.
     */
    public static String format(DomainEventSubscriberInformation information) {
        return information.formatRabbitMQQueueName();
    }

    /**
     * This method formats the queue name for the retry queue.
     * It appends the string "retry." to the beginning of the queue name.
     * 
     * @param information The DomainEventSubscriberInformation object.
     * @return The formatted queue name.
     */
    public static String formatRetry(DomainEventSubscriberInformation information) {
        return String.format("retry.%s", format(information));
    }

    /**
     * This method formats the queue name for the dead letter queue.
     * It appends the string "dead_letter." to the beginning of the queue name.
     * 
     * @param information The DomainEventSubscriberInformation object.
     * @return The formatted queue name.
     */
    public static String formatDeadLetter(DomainEventSubscriberInformation information) {
        return String.format("dead_letter.%s", format(information));
    }
}
