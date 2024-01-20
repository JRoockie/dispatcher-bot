package org.voetsky.dispatcherBot.service.messageutils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.configuration.localization.DispatcherLang;

@AllArgsConstructor
@Component
public class MakeMessage implements MessageUtils {

    private final DispatcherLang dispatcherLang;

    @Override
    public SendMessage generateSendMessageWithText(Update update, String text, boolean property) {
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());

        if (property) {
            sendMessage.setText(localize(update, text));
        } else {
            sendMessage.setText(text);
        }

        return sendMessage;
    }

    public String localize(Update update, String s) {
        return dispatcherLang.get(update.getMessage().getFrom().getLanguageCode(), s);
    }

}