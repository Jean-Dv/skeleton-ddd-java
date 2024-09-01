package dev.jean.shared.infrastructure.bus.query;

import org.springframework.context.ApplicationContext;

import dev.jean.shared.domain.Service;
import dev.jean.shared.domain.bus.query.Query;
import dev.jean.shared.domain.bus.query.QueryBus;
import dev.jean.shared.domain.bus.query.QueryHandler;
import dev.jean.shared.domain.bus.query.QueryHandlerExecutionError;
import dev.jean.shared.domain.bus.query.Response;

@Service
public class InMemoryQueryBus implements QueryBus {
    private final QueryHandlersInformation information;
    private final ApplicationContext context;

    public InMemoryQueryBus(QueryHandlersInformation information, ApplicationContext context) {
        this.information = information;
        this.context = context;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Response ask(Query query) throws QueryHandlerExecutionError {
        try {
            Class<? extends QueryHandler> queryHandlerClass = information.search(query.getClass());
            QueryHandler handler = context.getBean(queryHandlerClass);

            return handler.handle(query);
        } catch (Throwable e) {
            throw new QueryHandlerExecutionError(e);
        }
    }

}
