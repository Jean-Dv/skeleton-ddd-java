package dev.jean.shared.infrastructure.bus.event.rabbitmq;

import java.util.Collections;
import java.util.List;

import org.springframework.amqp.AmqpException;

import dev.jean.shared.domain.bus.event.DomainEvent;
import dev.jean.shared.domain.bus.event.EventBus;
import dev.jean.shared.infrastructure.bus.event.mysql.MysqlEventBus;

/**
 * This class is responsible for publishing events to RabbitMQ.
 */
public class RabbitMqEventBus implements EventBus {
    private final RabbitMqPublisher publisher;
    private final MysqlEventBus failoverPublisher;
    private final String exchangeName;

    public RabbitMqEventBus(
            RabbitMqPublisher publisher,
            MysqlEventBus failoverPublisher,
            String exchangeName) {
        this.publisher = publisher;
        this.failoverPublisher = failoverPublisher;
        this.exchangeName = "domain_events";
    }

    /**
     * This method is used to publish a multiple events.
     */
    @Override
    public void publish(List<DomainEvent> events) {
        events.forEach(this::publish);
    }

    /**
     * This method is used to publish an event to RabbitMQ.
     * In case of an error, it will publish the event to
     * the failover publisher.
     * 
     * @param event The event to publish.
     */
    private void publish(DomainEvent event) {
        try {
            this.publisher.publish(event, this.exchangeName);
        } catch (AmqpException e) {
            this.failoverPublisher.publish(Collections.singletonList(event));
        }
    }
}
