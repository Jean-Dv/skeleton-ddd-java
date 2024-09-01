package dev.jean.shared.domain.bus.command;

/**
 * This interface represents a command bus.
 */
public interface CommandBus {
    /**
     * Dispatches a command.
     * 
     * @param command the command
     * @throws CommandHandlerExecutionError
     */
    void dispatch(Command command) throws CommandHandlerExecutionError;
}
