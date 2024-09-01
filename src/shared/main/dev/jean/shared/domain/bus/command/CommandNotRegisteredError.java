package dev.jean.shared.domain.bus.command;

/**
 * This class represents a command not registered error.
 */
public final class CommandNotRegisteredError extends Exception {
    public CommandNotRegisteredError(Class<? extends Command> command) {
        super(String.format("The command <%s> has not a command handler associated", command.toString()));
    }
}
