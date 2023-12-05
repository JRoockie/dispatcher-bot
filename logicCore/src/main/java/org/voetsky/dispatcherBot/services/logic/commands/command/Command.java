package org.voetsky.dispatcherBot.services.logic.commands.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;

public interface Command {
    SendMessage handle(Update update);

    SendMessage callback(Update update);

    void changeState(Update update, UserState userState);

}