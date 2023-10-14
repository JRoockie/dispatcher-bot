package org.voetsky.dispatcherBot.commands;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandInterface {
    SendMessage handle(Update update);

    SendMessage callback(Update update);
}