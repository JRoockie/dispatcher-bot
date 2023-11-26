package org.voetsky.dispatcherBot.services.commandHandlerService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

public interface CommandHandler {

    HashMap<Boolean, SendMessage> updateReceived(Update update);

    HashMap<Boolean, SendMessage> buttonExecute(Update update);

    HashMap<Boolean, SendMessage> updateReceivedCommandEvoke(SendMessage s);

}
