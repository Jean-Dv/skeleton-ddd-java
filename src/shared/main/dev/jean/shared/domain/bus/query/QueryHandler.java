package dev.jean.shared.domain.bus.query;

/**
 * This interface represents a query handler.
 */
public interface QueryHandler<Q extends Query, R extends Response> {
    /**
     * This method allows to handle a query.
     * 
     * @param query The query
     * @return The response
     */
    R handle(Q query);
}
