package org.voetsky.dispatcherBot.services.logic.commands.commandFunctions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Chain {
    SendMessage putNextCommand(Update update, String command);
}
