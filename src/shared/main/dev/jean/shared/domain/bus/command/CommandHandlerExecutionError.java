package dev.jean.shared.domain.bus.command;

/**
 * This class represents a command handler execution error.
 */
public final class CommandHandlerExecutionError extends RuntimeException {
    public CommandHandlerExecutionError(Throwable cause) {
        super(cause);
    }

}
