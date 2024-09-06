package dev.jean.shared.infrastructure.bus.event.rabbitmq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.jean.shared.infrastructure.bus.event.DomainEventSubscribersInformation;
import dev.jean.shared.infrastructure.bus.event.DomainEventsInformation;
import dev.jean.shared.infrastructure.config.Parameter;
import dev.jean.shared.infrastructure.config.ParameterNotExist;

/**
 * This class is responsible for configuring the RabbitMQ event bus.
 * It creates the connection to the RabbitMQ server, declares the exchanges,
 * queues, and bindings, and returns the Declarables object.
 */
@Configuration
public class RabbitMqEventBusConfiguration {
    private final DomainEventSubscribersInformation domainEventSubscribersInformation;
    private final DomainEventsInformation domainEventsInformation;
    private final Parameter config;
    private final String exchangeName;

    public RabbitMqEventBusConfiguration(
            DomainEventSubscribersInformation domainEventSubscribersInformation,
            DomainEventsInformation domainEventsInformation,
            Parameter config) throws ParameterNotExist {
        this.domainEventSubscribersInformation = domainEventSubscribersInformation;
        this.domainEventsInformation = domainEventsInformation;
        this.config = config;
        this.exchangeName = config.get("RABBITMQ_EXCHANGE");
    }

    /**
     * This method creates the connection to the RabbitMQ server.
     *
     * @return The CachingConnectionFactory object.
     * @throws ParameterNotExist
     */
    @Bean
    protected CachingConnectionFactory connection() throws ParameterNotExist {
        CachingConnectionFactory factory = new CachingConnectionFactory();

        factory.setHost(this.config.get("RABBITMQ_HOST"));
        factory.setPort(this.config.getInt("RABBITMQ_PORT"));
        factory.setUsername(this.config.get("RABBITMQ_USER"));
        factory.setPassword(this.config.get("RABBITMQ_PASSWORD"));

        return factory;
    }

    /**
     * This method declares the exchanges, queues, and bindings for the RabbitMQ
     * event bus.
     *
     * @return The Declarables object.
     */
    @Bean
    protected Declarables declaration() {
        String retryExchangeName = RabbitMqExchangeNameFormatter.retry(this.exchangeName);
        String deadLetterExchangeName = RabbitMqExchangeNameFormatter.deadLetter(exchangeName);

        TopicExchange domainEventsExchange = new TopicExchange(exchangeName, true, false);
        TopicExchange retryDomainEventsExchange = new TopicExchange(retryExchangeName, true, false);
        TopicExchange deadLetterDomainEventsExchange = new TopicExchange(deadLetterExchangeName, true, false);

        List<Declarable> declarables = new ArrayList<>();

        declarables.add(domainEventsExchange);
        declarables.add(retryDomainEventsExchange);
        declarables.add(deadLetterDomainEventsExchange);

        Collection<Declarable> queuesAndBindings = this.declareQueuesAndBindings(
                domainEventsExchange,
                retryDomainEventsExchange,
                deadLetterDomainEventsExchange).stream().flatMap(Collection::stream).collect(Collectors.toList());

        declarables.addAll(queuesAndBindings);

        return new Declarables(declarables);
    }

    /**
     * This method declares the queues and bindings for the RabbitMQ event bus.
     * It creates the queues for the main queue, the retry queue, and the dead
     * letter queue, and the bindings for the domain events.
     * 
     * @param domainEventsExchange           The main exchange.
     * @param retryDomainEventsExchange      The retry exchange.
     * @param deadLetterDomainEventsExchange The dead letter exchange.
     * @return A collection of collections containing the queues and bindings.
     */
    private Collection<Collection<Declarable>> declareQueuesAndBindings(
            TopicExchange domainEventsExchange,
            TopicExchange retryDomainEventsExchange,
            TopicExchange deadLetterDomainEventsExchange) {
        return this.domainEventSubscribersInformation.all().stream().map(information -> {
            String queueName = RabbitMqQueueNameFormatter.format(information);
            String retryQueueName = RabbitMqQueueNameFormatter.formatRetry(information);
            String deadLetterQueueName = RabbitMqQueueNameFormatter.formatDeadLetter(information);

            Queue queue = QueueBuilder.durable(queueName).build();
            Queue retryQueue = QueueBuilder.durable(retryQueueName).withArguments(
                    retryQueueArguments(domainEventsExchange, queueName)).build();
            Queue deadLetterQueue = QueueBuilder.durable(deadLetterQueueName).build();

            Binding fromExchangeSameQueueBinding = BindingBuilder
                    .bind(queue)
                    .to(domainEventsExchange)
                    .with(queueName);

            Binding fromRetryExchangeSameQueueBinding = BindingBuilder
                    .bind(retryQueue)
                    .to(retryDomainEventsExchange)
                    .with(queueName);

            Binding fromDeadLetterExchangeSameQueueBinding = BindingBuilder
                    .bind(deadLetterQueue)
                    .to(deadLetterDomainEventsExchange)
                    .with(queueName);

            List<Binding> fromExchangeDomainEventsBindings = information.subscribedEvents().stream().map(
                    domainEventClass -> {
                        String eventName = domainEventsInformation.forClass(domainEventClass);
                        return BindingBuilder
                                .bind(queue)
                                .to(domainEventsExchange)
                                .with(eventName);
                    }).collect(Collectors.toList());

            List<Declarable> queuesAndBindings = new ArrayList<>();
            queuesAndBindings.add(queue);
            queuesAndBindings.add(fromExchangeSameQueueBinding);
            queuesAndBindings.addAll(fromExchangeDomainEventsBindings);

            queuesAndBindings.add(retryQueue);
            queuesAndBindings.add(fromRetryExchangeSameQueueBinding);

            queuesAndBindings.add(deadLetterQueue);
            queuesAndBindings.add(fromDeadLetterExchangeSameQueueBinding);

            return queuesAndBindings;
        }).collect(Collectors.toList());
    }

    /**
     * This method returns the arguments for the retry queue.
     *
     * @param exchange   The exchange.
     * @param routingKey The routing key.
     * @return The arguments for the retry queue.
     */
    private HashMap<String, Object> retryQueueArguments(TopicExchange exchange, String routingKey) {
        return new HashMap<String, Object>() {
            {
                put("x-dead-letter-exchange", exchange.getName());
                put("x-dead-letter-routing-key", routingKey);
                put("x-message-ttl", 1000);
            }
        };
    }
}
