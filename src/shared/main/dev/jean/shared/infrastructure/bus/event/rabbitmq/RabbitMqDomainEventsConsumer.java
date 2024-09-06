package dev.jean.shared.infrastructure.bus.event.rabbitmq;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.context.ApplicationContext;

import dev.jean.shared.domain.Service;
import dev.jean.shared.domain.Utils;
import dev.jean.shared.domain.bus.event.DomainEvent;
import dev.jean.shared.infrastructure.bus.event.DomainEventJsonDeserializer;
import dev.jean.shared.infrastructure.bus.event.DomainEventSubscribersInformation;

/**
 * This class is responsible for consuming domain events from RabbitMQ.
 * It consumes the events, deserializes them, and sends them to the subscribers.
 * If an error occurs, it sends the event to the retry or dead letter exchange.
 * 
 * @see DomainEventJsonDeserializer
 * @see DomainEventSubscribersInformation
 * @see RabbitMqPublisher
 */
@Service
public final class RabbitMqDomainEventsConsumer {
    private final String CONSUMER_NAME = "domain_events_consumer";
    private final Integer MAX_RETRIES = 2;
    private final DomainEventJsonDeserializer deserializer;
    private final ApplicationContext context;
    private final RabbitMqPublisher publisher;
    private final HashMap<String, Object> domainEventSubscribers = new HashMap<String, Object>();
    private final RabbitListenerEndpointRegistry registry;
    private final DomainEventSubscribersInformation information;

    public RabbitMqDomainEventsConsumer(
            DomainEventJsonDeserializer deserializer, ApplicationContext context,
            RabbitMqPublisher publisher, RabbitListenerEndpointRegistry registry,
            DomainEventSubscribersInformation information) {
        this.deserializer = deserializer;
        this.context = context;
        this.publisher = publisher;
        this.registry = registry;
        this.information = information;
    }

    /**
     * This method is used to consume domain events from RabbitMQ.
     * It starts the listener container and adds the queue names to it.
     */
    public void consume() {
        AbstractMessageListenerContainer container = (AbstractMessageListenerContainer) registry
                .getListenerContainer(this.CONSUMER_NAME);

        container.addQueueNames(this.information.rabbitMQFormattedNames());
        container.start();
    }

    /**
     * This method is used to consume a message from RabbitMQ.
     * It deserializes the message, gets the subscriber, and invokes the
     * subscriber's `on` method. If an error occurs, it sends the message to the
     * retry or dead letter exchange.
     * 
     * @param message The message to consume.
     * @throws Exception If an error occurs.
     */
    public void consumer(Message message) throws Exception {
        String serializedMessage = new String(message.getBody());
        DomainEvent domainEvent = this.deserializer.deserialize(serializedMessage);

        String queue = message.getMessageProperties().getConsumerQueue();

        Object subscriber = this.domainEventSubscribers.containsKey(queue)
                ? this.domainEventSubscribers.get(queue)
                : this.subscriberFor(queue);

        Method subscriberOnMethod = subscriber.getClass().getMethod("on", domainEvent.getClass());

        try {
            subscriberOnMethod.invoke(subscriber, domainEvent);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new Exception(String.format(
                    "The subscriber <%s> should implement a method `on` listening the domain event <%s>",
                    queue,
                    domainEvent.eventName()));
        } catch (Exception e) {
            this.handleConsumptionError(message, queue);
        }
    }

    /**
     * This method is used to handle a consumption error.
     * If the message has been redelivered too much, it sends the message to the
     * dead letter exchange. Otherwise, it sends the message to the retry exchange.
     * 
     * @param message
     * @param queue
     */
    private void handleConsumptionError(Message message, String queue) {
        if (this.hasBeenRedeliveredTooMuch(message)) {
            this.sendToDeadLetter(message, queue);
        } else {
            this.sendToRetry(message, queue);
        }
    }

    /**
     * This method is used to send a message to the retry exchange.
     * 
     * @param message The message to send.
     * @param queue   The queue name.
     */
    private void sendToRetry(Message message, String queue) {
        this.sendMessageTo(RabbitMqExchangeNameFormatter.retry("domain_events"), message, queue);
    }

    /**
     * This method is used to send a message to the dead letter exchange.
     * 
     * @param message The message to send.
     * @param queue   The queue name.
     */
    private void sendToDeadLetter(Message message, String queue) {
        this.sendMessageTo(RabbitMqExchangeNameFormatter.deadLetter("domain_events"), message, queue);
    }

    /**
     * This method is used to send a message to an exchange.
     * 
     * @param exchange The exchange name.
     * @param message  The message to send.
     * @param queue    The queue name.
     */
    private void sendMessageTo(String exchange, Message message, String queue) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();

        headers.put("redelivery_count", (int) headers.getOrDefault("redelivery_count", 0) + 1);

        MessageBuilder.fromMessage(message).andProperties(
                MessagePropertiesBuilder.newInstance()
                        .setContentEncoding("utf-8")
                        .setContentType("application/json")
                        .copyHeaders(headers)
                        .build());

        this.publisher.publish(message, exchange, queue);
    }

    /**
     * This method is used to check if a message has been redelivered too much.
     * 
     * @param message The message to check.
     * @return True if the message has been redelivered too much, false otherwise.
     */
    private boolean hasBeenRedeliveredTooMuch(Message message) {
        return (int) message.getMessageProperties().getHeaders().getOrDefault("redelivery_count",
                0) >= this.MAX_RETRIES;
    }

    /**
     * This method is used to get the subscriber for a queue.
     * 
     * @param queue The queue name.
     * @return The subscriber.
     */
    private Object subscriberFor(String queue) throws Exception {
        String[] queueParts = queue.split("\\.");
        String subscriberName = Utils.toCamelFirstLower(queueParts[queueParts.length - 1]);

        try {
            Object subscriber = this.context.getBean(subscriberName);
            this.domainEventSubscribers.put(queue, subscriber);

            return subscriber;
        } catch (Exception e) {
            throw new Exception(String.format("There are not registered subscribers for <%s> queue", queue));
        }
    }

}
