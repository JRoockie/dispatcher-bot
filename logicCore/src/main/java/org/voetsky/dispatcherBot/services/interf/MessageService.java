package org.voetsky.dispatcherBot.services.interf;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageService {
    SendMessage send(Update update, String text);
    SendMessage send(SendMessage sendMessage);
}
