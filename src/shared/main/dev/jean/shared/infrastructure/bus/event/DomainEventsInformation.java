package dev.jean.shared.infrastructure.bus.event;

import dev.jean.shared.domain.Service;
import dev.jean.shared.domain.bus.event.DomainEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;
import java.util.Objects;
import java.util.Map;

import org.reflections.Reflections;

/**
 * This class is responsible for indexing all domain events in the application.
 * It uses Reflections to scan the application for all classes that extend
 * DomainEvent. It then creates a map of the event name to the class.
 * This map is used by the DomainEventJsonDeserializer to deserialize the
 * events.
 */
@Service
public final class DomainEventsInformation {
    HashMap<String, Class<? extends DomainEvent>> indexedDomainEvents;

    public DomainEventsInformation() {
        Reflections reflections = new Reflections("dev.jean");
        Set<Class<? extends DomainEvent>> classes = reflections.getSubTypesOf(DomainEvent.class);

        try {
            indexedDomainEvents = this.formatEvents(classes);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the class of the domain event with the given name.
     * 
     * @param name The name of the domain event.
     * @return The class of the domain event.
     */
    public Class<? extends DomainEvent> forName(String name) {
        return indexedDomainEvents.get(name);
    }

    /**
     * Returns the name of the domain event class.
     * 
     * @param domainEventClass The class of the domain event.
     * @return The name of the domain event.
     */
    public String forClass(Class<? extends DomainEvent> domainEventClass) {
        return indexedDomainEvents.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), domainEventClass))
                .map(Map.Entry::getKey)
                .findFirst().orElse("");
    }

    /**
     * Formats the domain events into a map of event name to class.
     * 
     * @param domainEvents The set of domain events.
     * @return The map of event name to class.
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    private HashMap<String, Class<? extends DomainEvent>> formatEvents(
            Set<Class<? extends DomainEvent>> domainEvents)
            throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        HashMap<String, Class<? extends DomainEvent>> events = new HashMap<>();

        for (Class<? extends DomainEvent> domainEvent : domainEvents) {
            DomainEvent nullInstance = domainEvent.getConstructor().newInstance();
            events.put((String) domainEvent.getMethod("eventName").invoke(nullInstance), domainEvent);
        }
        return events;
    }
}
