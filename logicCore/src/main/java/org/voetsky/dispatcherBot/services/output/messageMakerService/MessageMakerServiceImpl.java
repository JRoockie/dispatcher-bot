package org.voetsky.dispatcherBot.services.output.messageMakerService;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Log4j
@Service
public class MessageMakerServiceImpl implements MessageMakerService {

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

    private SendMessage createSendMessage(Update update, String text, InlineKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(getIdFromUpdate(update));
        sendMessage.setText(text);
        if (markup != null) {
            sendMessage.setReplyMarkup(markup);
        }
        return sendMessage;
    }

}