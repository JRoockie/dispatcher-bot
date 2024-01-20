package org.voetsky.dispatcherBot.services.logic.commandHandlerService;

import jakarta.annotation.PostConstruct;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


public interface CommandHandler {

    @PostConstruct
    void initCommands();

    SendMessage updateReceived(Update update);

    boolean hasForceCallback(String text, Update update);

    SendMessage forceCallback(Update update, String text);

    SendMessage processCommand(Update update, String text, String chatId);

    SendMessage processHandle(Update update, String text, String chatId);

    SendMessage processCallBack(Update update, String chatId);

    SendMessage processButton(Update update, String text, String chatId);

    Boolean hasChain(SendMessage s);

    SendMessage processChain(SendMessage s, Update update);

    String getChatId(Update update);

    String getMessageText(Update update);

}
