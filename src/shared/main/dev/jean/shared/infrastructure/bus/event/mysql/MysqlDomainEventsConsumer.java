package dev.jean.shared.infrastructure.bus.event.mysql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Qualifier;
import jakarta.transaction.Transactional;

import dev.jean.shared.domain.Utils;
import dev.jean.shared.domain.bus.event.DomainEvent;
import dev.jean.shared.domain.bus.event.EventBus;
import dev.jean.shared.infrastructure.bus.event.DomainEventsInformation;

/**
 * This class is responsible for consuming events from the database.
 * It uses Hibernate to interact with the database.
 * 
 * @see EventBus
 */
public class MysqlDomainEventsConsumer {
    private final SessionFactory sessionFactory;
    private final DomainEventsInformation domainEventsInformation;
    private final EventBus bus;
    private final Integer CHUNKS = 200;
    private Boolean shouldStop = false;

    public MysqlDomainEventsConsumer(
            @Qualifier("session_factory") SessionFactory sessionFactory,
            DomainEventsInformation domainEventsInformation,
            EventBus bus) {
        this.sessionFactory = sessionFactory;
        this.domainEventsInformation = domainEventsInformation;
        this.bus = bus;
    }

    /**
     * Consumes events from the database. It will consume events
     * until the stop method is called.
     */
    @Transactional
    public void consume() {
        while (!shouldStop) {
            NativeQuery<Object[]> query = sessionFactory.getCurrentSession().createNativeQuery(
                    "SELECT * FROM domain_events ORDER BY occurred_on ASC LIMIT :chunk", Object[].class);

            query.setParameter("chunk", this.CHUNKS);

            List<Object[]> events = query.list();

            try {
                for (Object[] event : events) {
                    this.executeSubscribers(
                            (String) event[0],
                            (String) event[1],
                            (String) event[2],
                            (String) event[3],
                            (Timestamp) event[4]);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException
                    | InstantiationException e) {
                e.printStackTrace();
            }

            sessionFactory.getCurrentSession().clear();
        }
    }

    /**
     * Stops the consumer.
     */
    public void stop() {
        this.shouldStop = true;
    }

    /**
     * Executes the subscribers of the event.
     * 
     * @param id          Event ID. 
     * @param aggregateId Aggregate ID. 
     * @param eventName   Event name.
     * @param body        Event body.
     * @param occurredOn  Event occurred on.
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private void executeSubscribers(
            String id, String aggregateId, String eventName, String body, Timestamp occurredOn)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<? extends DomainEvent> domainEventClass = domainEventsInformation.forName(eventName);

        DomainEvent nullInstance = domainEventClass.getConstructor().newInstance();

        Method fromPrimitivesMethod = domainEventClass.getMethod(
                "fromPrimitives",
                String.class,
                HashMap.class,
                String.class,
                String.class);

        Object domainEvent = fromPrimitivesMethod.invoke(
                nullInstance,
                aggregateId,
                Utils.jsonDecode(body),
                id,
                Utils.dateToString(occurredOn));

        this.bus.publish(Collections.singletonList((DomainEvent) domainEvent));
    }
}
