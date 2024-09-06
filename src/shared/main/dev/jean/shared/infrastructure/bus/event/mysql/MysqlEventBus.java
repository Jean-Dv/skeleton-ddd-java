package dev.jean.shared.infrastructure.bus.event.mysql;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

import dev.jean.shared.domain.Utils;
import dev.jean.shared.domain.bus.event.DomainEvent;
import dev.jean.shared.domain.bus.event.EventBus;

/**
 * This class is responsible for publishing events to the database.
 * It uses Hibernate to interact with the database.
 */
public final class MysqlEventBus implements EventBus {
    private final SessionFactory sessionFactory;

    public MysqlEventBus(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Publishes a list of events to the database.
     * 
     * @param events List of events to be published.
     */
    @Override
    public void publish(List<DomainEvent> events) {
        events.forEach(this::publish);
    }

    /**
     * Publishes a single event to the database.
     * 
     * @param event Event to be published.
     */
    private void publish(DomainEvent event) {
        String id = event.eventId();
        String aggregateId = event.aggregateId();
        String name = event.eventName();
        HashMap<String, Serializable> body = event.toPrimitives();
        String occurredOn = event.occurredOn();

        NativeQuery<Object> query = sessionFactory.getCurrentSession().createNativeQuery(
                "INSERT INTO domain_events (id, aggregate_id, name, body, occurred_on) " +
                        "VALUE (:id, :aggregateId, :name, :body, :occurredOn)",
                Object.class);

        query.setParameter("id", id)
                .setParameter("aggregateId", aggregateId)
                .setParameter("name", name)
                .setParameter("body", Utils.jsonEncode(body))
                .setParameter("occurredOn", occurredOn);

        query.executeUpdate();
    }

}
