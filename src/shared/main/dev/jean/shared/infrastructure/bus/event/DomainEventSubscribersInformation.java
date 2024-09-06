package dev.jean.shared.infrastructure.bus.event;

import dev.jean.shared.domain.Service;
import dev.jean.shared.domain.bus.event.DomainEventSubscriber;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.reflections.Reflections;

/**
 * This class is responsible for scanning the project and finding all the
 * classes annotated with the DomainEventSubscriber annotation. It will then
 * store the information in a HashMap, where the key is the class and the value
 * is a DomainEventSubscriberInformation object.
 */
@Service
public final class DomainEventSubscribersInformation {
    private HashMap<Class<?>, DomainEventSubscriberInformation> information;

    public DomainEventSubscribersInformation(
            HashMap<Class<?>, DomainEventSubscriberInformation> information) {
        this.information = information;
    }

    public DomainEventSubscribersInformation() {
        this(scanDomainEventSubscribers());
    }

    /**
     * This method uses the Reflections library to scan the project and find all
     * classes annotated with the DomainEventSubscriber annotation. It will then
     * store the information in a HashMap, where the key is the class and the value
     * is a DomainEventSubscriberInformation object.
     * 
     * @return The HashMap containing the information.
     */
    private static HashMap<Class<?>, DomainEventSubscriberInformation> scanDomainEventSubscribers() {
        Reflections reflections = new Reflections("dev.jean");
        Set<Class<?>> subscribers = reflections.getTypesAnnotatedWith(DomainEventSubscriber.class);

        HashMap<Class<?>, DomainEventSubscriberInformation> subscribersInformation = new HashMap<>();

        for (Class<?> subscriberClass : subscribers) {
            DomainEventSubscriber annotation = subscriberClass.getAnnotation(DomainEventSubscriber.class);

            subscribersInformation.put(
                    subscriberClass,
                    new DomainEventSubscriberInformation(subscriberClass, Arrays.asList(annotation.value())));
        }

        return subscribersInformation;
    }

    /**
     * This method returns the DomainEventSubscriberInformation object for a given
     * class.
     * 
     * @return The DomainEventSubscriberInformation object.
     */
    public Collection<DomainEventSubscriberInformation> all() {
        return this.information.values();
    }

    /**
     * Formats the names of the RabbitMQ queues to which the subscribers are
     * subscribed.
     * 
     * @return An array containing the formatted names.
     */
    public String[] rabbitMQFormattedNames() {
        return this.information.values()
                .stream()
                .map(DomainEventSubscriberInformation::formatRabbitMQQueueName)
                .distinct()
                .toArray(String[]::new);
    }

}
