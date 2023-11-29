package org.voetsky.dispatcherBot.services.messageMakerService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.HashMap;

public interface MessageMakerService {

    Long getIdFromUpdate(Update update);

    SendMessage makeSendMessage(Update update, String string);

    SendMessage makeSendMessage(Update update, String text, InlineKeyboardMarkup i);
}
