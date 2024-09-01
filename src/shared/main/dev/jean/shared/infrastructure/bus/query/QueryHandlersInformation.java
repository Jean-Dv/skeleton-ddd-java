package dev.jean.shared.infrastructure.bus.query;

import org.reflections.Reflections;

import dev.jean.shared.domain.Service;
import dev.jean.shared.domain.bus.query.Query;
import dev.jean.shared.domain.bus.query.QueryHandler;
import dev.jean.shared.domain.bus.query.QueryNotRegisteredError;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Set;

/**
 * This class represents the query handlers information.
 */
@Service
public final class QueryHandlersInformation {
    @SuppressWarnings("rawtypes")
    HashMap<Class<? extends Query>, Class<? extends QueryHandler>> indexedQueryHandlers;

    @SuppressWarnings("rawtypes")
    public QueryHandlersInformation() {
        Reflections reflections = new Reflections("dev.jean");
        Set<Class<? extends QueryHandler>> classes = reflections
                .getSubTypesOf(QueryHandler.class);

        this.indexedQueryHandlers = this.formatHandlers(classes);
    }

    /**
     * Searches for a query handler.
     * 
     * @param queryClass Query class.
     * @return Query handler class.
     * @throws QueryNotRegisteredError
     */
    @SuppressWarnings("rawtypes")
    public Class<? extends QueryHandler> search(Class<? extends Query> queryClass)
            throws QueryNotRegisteredError {
        Class<? extends QueryHandler> queryHandlerClass = indexedQueryHandlers.get(queryClass);

        if (null == queryHandlerClass) {
            throw new QueryNotRegisteredError(queryClass);
        }

        return queryHandlerClass;
    }

    /**
     * Formats the query handlers.
     * 
     * @param queryHandlers Query handlers.
     * @return Formatted query handlers.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private HashMap<Class<? extends Query>, Class<? extends QueryHandler>> formatHandlers(
            Set<Class<? extends QueryHandler>> queryHandlers) {
        HashMap<Class<? extends Query>, Class<? extends QueryHandler>> handlers = new HashMap<>();

        for (Class<? extends QueryHandler> handler : queryHandlers) {
            ParameterizedType paramType = (ParameterizedType) handler.getGenericInterfaces()[0];
            Class<? extends Query> queryClass = (Class<? extends Query>) paramType.getActualTypeArguments()[0];

            handlers.put(queryClass, handler);
        }

        return handlers;
    }
}
