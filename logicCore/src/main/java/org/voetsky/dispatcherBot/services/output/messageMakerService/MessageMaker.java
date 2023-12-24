package org.voetsky.dispatcherBot.services.output.messageMakerService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface MessageMaker {

    Long getIdFromUpdate(Update update);

//    SendMessage makeSendMessage(Update update, String string);

//    SendMessage makeSendMessage(Update update, String text, InlineKeyboardMarkup markup);

    SendMessage makeSendMessage(Update update,
                                String text,
                                boolean fromProperties);

    SendMessage makeSendMessage(Update update, String text,
                                InlineKeyboardMarkup markup,
                                boolean fromProperties);

    SendMessage createSendMessage(Update update, String text, InlineKeyboardMarkup markup, boolean fromProperties);

    String getLanguageFromTg(Update update);

    String getTextFromProperties(Update update, String text);
}
