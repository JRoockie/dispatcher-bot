package org.voetsky.dispatcherBot.services.messageMakerService;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.HashMap;

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
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(getIdFromUpdate(update));
            sendMessage.setReplyToMessageId(update.getMessage().getMessageId());
            sendMessage.setText(text);
            return sendMessage;
        } catch (Exception e) {
            return new SendMessage("874396856", "Ошибка");
        }

    }

    @Override
    public SendMessage makeSendMessage(Update update, String text, InlineKeyboardMarkup i) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(getIdFromUpdate(update));
            sendMessage.setReplyMarkup(i);
            sendMessage.setReplyToMessageId(update.getMessage().getMessageId());
            sendMessage.setText(text);
            return sendMessage;
        } catch (Exception e) {
            return new SendMessage("874396856", "Ошибка");
        }

    }
}