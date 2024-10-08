package dev.jean.shared.infrastructure.bus.event;

import dev.jean.shared.domain.Service;
import dev.jean.shared.domain.Utils;
import dev.jean.shared.domain.bus.event.DomainEvent;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * This class is responsible for deserializing domain events from JSON.
 * It uses the DomainEventsInformation class to get the class of the domain
 * event from the event name.
 */
@Service
public class DomainEventJsonDeserializer {
    private final DomainEventsInformation information;

    public DomainEventJsonDeserializer(DomainEventsInformation information) {
        this.information = information;
    }

    /**
     * Deserializes a domain event from a JSON string.
     * 
     * @param body The JSON string to deserialize.
     * @return The domain event.
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    public DomainEvent deserialize(String body)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        HashMap<String, Serializable> eventData = Utils.jsonDecode(body);
        HashMap<String, Serializable> data = (HashMap<String, Serializable>) eventData.get("data");
        HashMap<String, Serializable> attributes = (HashMap<String, Serializable>) data.get("attributes");
        Class<? extends DomainEvent> domainEventClass = information.forName((String) data.get("type"));

        DomainEvent nullInstance = domainEventClass.getConstructor().newInstance();

        Method fromPrimitivesMethod = domainEventClass.getMethod(
                "fromPrimitives",
                String.class,
                HashMap.class,
                String.class,
                String.class);

        Object domainEvent = fromPrimitivesMethod.invoke(
                nullInstance,
                (String) attributes.get("id"),
                attributes,
                (String) data.get("id"),
                (String) data.get("occurred_on"));

        return (DomainEvent) domainEvent;
    }
}
