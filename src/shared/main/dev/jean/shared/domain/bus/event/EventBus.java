package dev.jean.shared.domain.bus.event;

import java.util.List;

/**
 * This interface represents the event bus. It is used to publish events.
 */
public interface EventBus {
    /**
     * This method is used to publish a multiple events.
     * 
     * @param events The events to publish.
     */
    void publish(final List<DomainEvent> events);
}
