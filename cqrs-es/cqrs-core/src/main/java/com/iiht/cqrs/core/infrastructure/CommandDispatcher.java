package com.iiht.cqrs.core.infrastructure;

import com.iiht.cqrs.core.commands.BaseCommand;
import com.iiht.cqrs.core.commands.CommandHandlerMethod;

public interface CommandDispatcher {
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler);
    void send(BaseCommand command);
}
