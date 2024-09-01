package dev.jean.shared.infrastructure.bus.command;

import org.springframework.context.ApplicationContext;

import dev.jean.shared.domain.Service;
import dev.jean.shared.domain.bus.command.Command;
import dev.jean.shared.domain.bus.command.CommandBus;
import dev.jean.shared.domain.bus.command.CommandHandler;
import dev.jean.shared.domain.bus.command.CommandHandlerExecutionError;

@Service
public class InMemoryCommandBus implements CommandBus {
    private final CommandHandlersInformation information;
    private final ApplicationContext context;

    public InMemoryCommandBus(CommandHandlersInformation information, ApplicationContext context) {
        this.information = information;
        this.context = context;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void dispatch(Command command) throws CommandHandlerExecutionError {
        try {
            Class<? extends CommandHandler> commandHandlerClass = information.search(command.getClass());
            CommandHandler handler = context.getBean(commandHandlerClass);

            handler.handle(command);
        } catch (Exception e) {
            throw new CommandHandlerExecutionError(e);
        }
    }
}
