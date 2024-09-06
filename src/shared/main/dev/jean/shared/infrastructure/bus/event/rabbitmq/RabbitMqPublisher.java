package dev.jean.shared.infrastructure.bus.event.rabbitmq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import dev.jean.shared.domain.Service;
import dev.jean.shared.domain.bus.event.DomainEvent;
import dev.jean.shared.infrastructure.bus.event.DomainEventJsonSerializer;

/**
 * This class is responsible for publishing events to RabbitMQ.
 */
@Service
public final class RabbitMqPublisher {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMqPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * This method is used to publish an event to RabbitMQ.
     * 
     * @param domainEvent  The event to publish.
     * @param exchangeName The exchange name.
     * @throws AmqpException
     */
    public void publish(DomainEvent domainEvent, String exchangeName) throws AmqpException {
        String serializedDomainEvent = DomainEventJsonSerializer.serialize(domainEvent);

        Message message = new Message(
                serializedDomainEvent.getBytes(),
                MessagePropertiesBuilder
                        .newInstance()
                        .setContentEncoding("utf-8")
                        .setContentType("application/json")
                        .build());

        rabbitTemplate.send(exchangeName, domainEvent.eventName(), message);
    }

    /**
     * This method is used to publish a message to RabbitMQ.
     * 
     * @param domainEvent  The message to publish.
     * @param exchangeName The exchange name.
     * @param routingKey   The routing key.
     * @throws AmqpException
     */
    public void publish(Message domainEvent, String exchangeName, String routingKey) throws AmqpException {
        rabbitTemplate.send(exchangeName, routingKey, domainEvent);
    }
}
