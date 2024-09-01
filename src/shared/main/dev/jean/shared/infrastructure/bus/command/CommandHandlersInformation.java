package dev.jean.shared.infrastructure.bus.command;

import dev.jean.shared.domain.Service;
import dev.jean.shared.domain.bus.command.Command;
import dev.jean.shared.domain.bus.command.CommandHandler;
import dev.jean.shared.domain.bus.command.CommandNotRegisteredError;

import java.util.HashMap;
import java.util.Set;
import java.lang.reflect.ParameterizedType;

import org.reflections.Reflections;

@Service
public class CommandHandlersInformation {
    @SuppressWarnings("rawtypes")
    HashMap<Class<? extends Command>, Class<? extends CommandHandler>> indexedCommandHandlers;

    @SuppressWarnings("rawtypes")
    public CommandHandlersInformation() {
        Reflections reflections = new Reflections("dev.jean");
        Set<Class<? extends CommandHandler>> classes = reflections.getSubTypesOf(CommandHandler.class);

        indexedCommandHandlers = this.formatHandlers(classes);
    }

    @SuppressWarnings("rawtypes")
    public Class<? extends CommandHandler> search(Class<? extends Command> commandClass)
            throws CommandNotRegisteredError {
        Class<? extends CommandHandler> commandHandlerClass = indexedCommandHandlers.get(commandClass);

        if (null == commandHandlerClass) {
            throw new CommandNotRegisteredError(commandClass);
        }

        return commandHandlerClass;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private HashMap<Class<? extends Command>, Class<? extends CommandHandler>> formatHandlers(
            Set<Class<? extends CommandHandler>> commandHandlers) {
        HashMap<Class<? extends Command>, Class<? extends CommandHandler>> handlers = new HashMap<>();

        for (Class<? extends CommandHandler> handler : commandHandlers) {
            ParameterizedType paramType = (ParameterizedType) handler.getGenericInterfaces()[0];
            Class<? extends Command> commandClass = (Class<? extends Command>) paramType.getActualTypeArguments()[0];

            handlers.put(commandClass, handler);
        }

        return handlers;
    }
}
