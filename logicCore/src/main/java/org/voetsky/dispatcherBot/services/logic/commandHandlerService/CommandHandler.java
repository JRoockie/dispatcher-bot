package org.voetsky.dispatcherBot.services.logic.commandHandlerService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler {

    SendMessage updateReceived(Update update);

    SendMessage processCommand(Update update, String text, String chatId);

    SendMessage processHandle(Update update, String text, String chatId);

    SendMessage processCallBack(Update update, String chatId);

    SendMessage processButton(Update update, String text, String chatId);

    Boolean hasChain(SendMessage s);

    SendMessage processChain(SendMessage s, Update update);

    String getChatId(Update update);

    String getMessageText(Update update);

    SendMessage forceEvokePreviousCommand(Update update);

}
