package dev.jean.shared.domain;

import dev.jean.shared.domain.bus.event.DomainEvent;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents an aggregate root.
 */
public abstract class AggregateRoot {
    private List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * Returns all events that occurred in the aggregate.
     * 
     * @return List of domain events.
     */
    final public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = this.domainEvents;
        this.domainEvents = Collections.emptyList();
        return events;
    }

    /**
     * Records an event in the aggregate.
     * 
     * @param event Event to record.
     */
    final protected void record(DomainEvent event) {
        this.domainEvents.add(event);
    }
}
