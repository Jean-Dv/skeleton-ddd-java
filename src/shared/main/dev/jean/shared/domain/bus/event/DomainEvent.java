package dev.jean.shared.domain.bus.event;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.io.Serializable;

import dev.jean.shared.domain.Utils;

/**
 * Domain event class. Represents an event that occurred in the domain.
 */
public abstract class DomainEvent {
    private String aggregateId;
    private String eventId;
    private String occurredOn;

    public DomainEvent(String aggregateId) {
        this.aggregateId = aggregateId;
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Utils.dateToString(LocalDateTime.now());
    }

    public DomainEvent(String aggregateId, String eventId, String occurredOn) {
        this.aggregateId = aggregateId;
        this.eventId = eventId;
        this.occurredOn = occurredOn;
    }

    protected DomainEvent() {
    }

    /**
     * Returns the event name.
     * 
     * @return Event name.
     */
    public abstract String eventName();

    /**
     * Returns the event body.
     * 
     * @return Event body.
     */
    public abstract HashMap<String, Serializable> toPrimitives();

    /**
     * Creates a domain event from primitives.
     * 
     * @param aggregateId Aggregate ID.
     * @param body        Event body.
     * @param eventId     Event ID.
     * @param occurredOn  Event occurred on.
     * @return Domain event.
     */
    public abstract DomainEvent fromPrimitives(
            String aggregateId,
            HashMap<String, Serializable> body,
            String eventId,
            String occurredOn);

    

    public String aggregateId() {
        return aggregateId;
    }

    public String eventId() {
        return eventId;
    }

    public String occurredOn() {
        return occurredOn;
    }
}
