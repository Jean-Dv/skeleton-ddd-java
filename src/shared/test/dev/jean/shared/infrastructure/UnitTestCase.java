package dev.jean.shared.infrastructure;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import dev.jean.shared.domain.UuidGenerator;
import dev.jean.shared.domain.bus.event.DomainEvent;
import dev.jean.shared.domain.bus.event.EventBus;
import dev.jean.shared.domain.bus.query.Query;
import dev.jean.shared.domain.bus.query.QueryBus;
import dev.jean.shared.domain.bus.query.Response;

/**
 * This class is a base class for unit test cases.
 */
public abstract class UnitTestCase {
    protected EventBus eventBus;
    protected QueryBus queryBus;
    protected UuidGenerator uuidGenerator;

    /**
     * Set up the unit test case.
     */
    @BeforeEach
    protected void setUp() {
        eventBus = mock(EventBus.class);
        queryBus = mock(QueryBus.class);
        uuidGenerator = mock(UuidGenerator.class);
    }

    /**
     * Verify that the event bus has published the given domain events.
     * 
     * @param domainEvents The domain events.
     */
    public void shouldHavePublished(List<DomainEvent> domainEvents) {
        verify(this.eventBus, atLeastOnce()).publish(domainEvents);
    }

    /**
     * Verify that the event bus has published the given domain event.
     * 
     * @param domainEvent The domain event.
     */
    public void shouldHavePublished(DomainEvent domainEvent) {
        this.shouldHavePublished(Collections.singletonList(domainEvent));
    }

    /**
     * When the UUID generator generates a UUID, it should return the
     * given UUID.
     * 
     * @param uuid The UUID.
     */
    public void shouldGenerateUuid(String uuid) {
        when(this.uuidGenerator.generate()).thenReturn(uuid);
    }

    /**
     * When the query bus is asked a query, it should return the given
     * response.
     * 
     * @param query    The query.
     * @param response The response.
     */
    public void shouldAsk(Query query, Response response) {
        when(this.queryBus.ask(query)).thenReturn(response);
    }
}
