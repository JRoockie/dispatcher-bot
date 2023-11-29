package org.voetsky.dispatcherBot.services.commandHandlerService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

public interface CommandHandler {

    SendMessage updateReceived(Update update);

    SendMessage buttonExecute(Update update);

}
