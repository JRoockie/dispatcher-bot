package org.voetsky.dispatcherBot.services.output.messageMakerService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voetsky.dispatcherBot.configuration.LogicCoreLocalization.LogicCoreLocalization;

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
    public SendMessage makeSendMessage(Update update, String text) {
            return createSendMessage(update, text, null);
    }

    @Override
    public SendMessage makeSendMessage(Update update, String text, InlineKeyboardMarkup markup) {
            return createSendMessage(update, text, markup);
    }

    public SendMessage createSendMessage(Update update, String text, InlineKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(getIdFromUpdate(update));
        sendMessage.setText(getTextFromProperties(update, text));
        if (markup != null) {
            sendMessage.setReplyMarkup(markup);
        }
        return sendMessage;
    }

    public String getLanguageFromTg(Update update){
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getLanguageCode();
        } else {
            return update.getMessage().getFrom().getLanguageCode();
        }
    }

    public String getTextFromProperties(Update update, String text) {
        String lang = getLanguageFromTg(update);
        if (localization.get(lang, text) != null){
            return localization.get(lang, text);
        }
        return text;
    }

    public SendMessage processError(Exception e, Update update) {
        if (log.isDebugEnabled()) {
            log.error("ERROR", e);
        }
        String t = getTextFromProperties(update, e.getMessage());
        return makeSendMessage(update, t);
    }

}