package org.voetsky.dispatcherBot.commandLogic.commandHandlerService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler {

    SendMessage updateReceived(Update update);

    SendMessage buttonExecute(Update update);

}
