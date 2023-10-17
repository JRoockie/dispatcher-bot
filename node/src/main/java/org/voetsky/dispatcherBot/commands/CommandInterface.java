package org.voetsky.dispatcherBot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;

public interface CommandInterface {
    SendMessage handle(Update update);

    SendMessage callback(Update update);

    void changeState(Update update, UserState userState);

}