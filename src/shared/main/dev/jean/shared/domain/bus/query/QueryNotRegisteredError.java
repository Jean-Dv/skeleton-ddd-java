package dev.jean.shared.domain.bus.query;

/**
 * This class represents an error that occurs when a query hasn't a query
 * handler associated.
 */
public final class QueryNotRegisteredError extends Exception {
    public QueryNotRegisteredError(Class<? extends Query> query) {
        super(String.format("The query <%s> hasn't a query handler associated", query.toString()));
    }
}
