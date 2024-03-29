package org.voetsky.dispatcherBot.services.output.messageMakerService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voetsky.dispatcherBot.localization.LogicCoreLocalization;

@Log4j
@AllArgsConstructor
@Service
public class MessageMakerService implements MessageMaker {

    private final LogicCoreLocalization localization;

    @Override
    public Long getIdFromUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        } else {
            return update.getMessage().getChatId();
        }
    }

    @Override
    public SendMessage makeSendMessage(Update update,
                                       String text,
                                       boolean property) {
        return createSendMessage(update, text, null, property);
    }

    @Override
    public SendMessage makeSendMessage(Update update, String text,
                                       InlineKeyboardMarkup markup,
                                       boolean fromProperties) {

        return createSendMessage(update, text, markup, fromProperties);
    }

    public SendMessage createSendMessage(Update update, String text, InlineKeyboardMarkup markup, boolean property) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(getIdFromUpdate(update));
        if (property) {
            sendMessage.setText(getTextFromProperties(update, text));
        } else {
            sendMessage.setText(text);
        }
        if (markup != null) {
            sendMessage.setReplyMarkup(markup);
        }
        return sendMessage;
    }

    public String getLanguageFromTg(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getLanguageCode();
        } else {
            return update.getMessage().getFrom().getLanguageCode();
        }
    }

    public String getTextFromProperties(Update update, String text) {
        String lang = getLanguageFromTg(update);
        if (localization.get(lang, text) != null) {
            return localization.get(lang, text);
        }
        return text;
    }

    public String getTextFromProperties(String text) {
        if (localization.getKeyFromDefaultLang( text) != null) {
            return localization.getKeyFromDefaultLang(text);
        }
        return text;
    }

    public SendMessage processError(Exception e, Update update) {
        if (log.isDebugEnabled()) {
            log.error("ERROR", e);
        }
        String t = getTextFromProperties(update, e.getMessage());
        return makeSendMessage(update, t, false);
    }

}