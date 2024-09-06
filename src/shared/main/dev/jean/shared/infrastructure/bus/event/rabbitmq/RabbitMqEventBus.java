package dev.jean.shared.infrastructure.bus.event.rabbitmq;

import java.util.List;

import org.springframework.amqp.AmqpException;

import dev.jean.shared.domain.bus.event.DomainEvent;
import dev.jean.shared.domain.bus.event.EventBus;

/**
 * This class is responsible for publishing events to RabbitMQ.
 */
public class RabbitMqEventBus implements EventBus {
    private final RabbitMqPublisher publisher;
    private final String exchangeName;

    public RabbitMqEventBus(RabbitMqPublisher publisher, String exchangeName) {
        this.publisher = publisher;
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
     * 
     * @param event The event to publish.
     */
    private void publish(DomainEvent event) {
        try {
            this.publisher.publish(event, this.exchangeName);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }
}
