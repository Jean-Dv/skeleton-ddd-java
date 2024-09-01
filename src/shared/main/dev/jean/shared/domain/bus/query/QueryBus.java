package dev.jean.shared.domain.bus.query;

/**
 * This interface represents a query bus.
 */
public interface QueryBus {
    /**
     * This method allows to ask a query.
     * 
     * @param <R>   the response type
     * @param query the query
     * @return the response
     * @throws QueryHandlerExecutionError
     */
    <R extends Response> R ask(Query query) throws QueryHandlerExecutionError;
}
