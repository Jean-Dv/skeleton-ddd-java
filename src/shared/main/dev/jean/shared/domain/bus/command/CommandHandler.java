package dev.jean.shared.domain.bus.command;

/**
 * This interface represents a command handler.
 */
public interface CommandHandler<T extends Command> {
    /**
     * Handles a command.
     * 
     * @param command the command
     */
    void handle(T command);
}
