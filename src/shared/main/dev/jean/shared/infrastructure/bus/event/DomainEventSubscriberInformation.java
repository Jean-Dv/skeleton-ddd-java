package dev.jean.shared.infrastructure.bus.event;

import java.util.List;

import dev.jean.shared.domain.Utils;
import dev.jean.shared.domain.bus.event.DomainEvent;

/**
 * This class contains information about a domain event subscriber.
 * It contains the subscriber class and the list of events it subscribes to.
 * This information is used to create a RabbitMQ queue for the subscriber.
 * The queue name is formatted as "jean.{context}.{module}.{class}".
 */
public final class DomainEventSubscriberInformation {
    private final Class<?> subscriberClass;
    private final List<Class<? extends DomainEvent>> subscribedEvents;

    public DomainEventSubscriberInformation(Class<?> subscriberClass,
            List<Class<? extends DomainEvent>> subscribedEvents) {
        this.subscriberClass = subscriberClass;
        this.subscribedEvents = subscribedEvents;
    }

    /**
     * Returns the subscriber class.
     * 
     * @return Subscriber class.
     */
    public Class<?> subscriberClass() {
        return subscriberClass;
    }

    /**
     * Returns the list of events the subscriber subscribes to.
     * 
     * @return List of events.
     */
    public List<Class<? extends DomainEvent>> subscribedEvents() {
        return subscribedEvents;
    }

    /**
     * Returns the context name.
     * 
     * @return Context name.
     * 
     */
    public String contextName() {
        String[] nameParts = subscriberClass.getName().split("\\.");

        return nameParts[2];
    }

    /**
     * Returns the module name.
     * 
     * @return Module name.
     */
    public String moduleName() {
        String[] nameParts = subscriberClass.getName().split("\\.");

        return nameParts[3];
    }

    /**
     * Returns the class name.
     * 
     * @return Class name.
     */
    public String className() {
        String[] nameParts = subscriberClass.getName().split("\\.");

        return nameParts[nameParts.length - 1];
    }

    /**
     * Formats the RabbitMQ queue name.
     * 
     * @return Formatted queue name.
     
     */
    public String formatRabbitMQQueueName() {
        return String.format("jean.%s.%s.%s", this.contextName(), this.moduleName(), Utils.toSnake(this.className()));
    }
}
