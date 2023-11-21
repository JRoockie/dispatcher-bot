package org.voetsky.dispatcherBot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.services.interf.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public SendMessage send(Update update, String text) {
        SendMessage sendMessage = new SendMessage();

        if (update.hasCallbackQuery()) {
            sendMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            sendMessage.setText(text);
            return sendMessage;
        } else {
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage.setText(text);
            return sendMessage;
        }
    }

    @Override
    public SendMessage send(SendMessage sendMessage) {
        return sendMessage;
    }
}
