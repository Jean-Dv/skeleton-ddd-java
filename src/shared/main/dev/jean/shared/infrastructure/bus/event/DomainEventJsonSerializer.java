package dev.jean.shared.infrastructure.bus.event;

import java.io.Serializable;
import java.util.HashMap;

import dev.jean.shared.domain.Utils;
import dev.jean.shared.domain.bus.event.DomainEvent;

/**
 * This class is responsible for serializing domain events to JSON.
 * It takes a DomainEvent and converts it to a JSON string.
 */
public final class DomainEventJsonSerializer {

    /**
     * Serializes a domain event to a JSON string.
     * 
     * @param domainEvent The domain event to serialize.
     * @return The JSON string representation of the domain event.
     */
    public static String serialize(DomainEvent domainEvent) {
        HashMap<String, Serializable> attributes = domainEvent.toPrimitives();
        attributes.put("id", domainEvent.aggregateId());

        return Utils.jsonEncode(new HashMap<String, Serializable>() {
            {
                put("data", new HashMap<String, Serializable>() {
                    {
                        put("id", domainEvent.eventId());
                        put("type", domainEvent.eventName());
                        put("occurred_on", domainEvent.occurredOn());
                        put("attributes", attributes);
                    }
                });
                put("meta", new HashMap<String, Serializable>());
            }
        });
    }
}
