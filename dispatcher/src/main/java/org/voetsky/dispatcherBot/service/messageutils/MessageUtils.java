package org.voetsky.dispatcherBot.service.messageutils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageUtils {

    SendMessage generateSendMessageWithText(Update update, String text, boolean property);

}
