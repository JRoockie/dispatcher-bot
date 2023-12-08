package org.voetsky.dispatcherBot.service.messageutils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.service.dispatcherLocalization.DispatcherLang;

@AllArgsConstructor
@Component
public class MakeMessage implements MessageUtils {
    private final DispatcherLang dispatcherLang;

    public SendMessage generateSendMessageWithText(Update update, String text) {
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(localize(update, text));
        return sendMessage;
    }

    public String localize(Update update, String s) {
        return dispatcherLang.get(update.getMessage().getFrom().getLanguageCode(), s);
    }

}