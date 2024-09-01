package dev.jean.shared.domain.bus.query;

/**
 * This class represents an error during the execution of a query handler.
 */
public class QueryHandlerExecutionError extends RuntimeException {
    public QueryHandlerExecutionError(Throwable cause) {
        super(cause);
    }
}
