package org.voetsky.dispatcherBot.services.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.services.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public SendMessage send(Update update, String text) {
        SendMessage sendMessage = new SendMessage();

        if (update.hasCallbackQuery()) {
            sendMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            sendMessage.setText(text + " " + update.getCallbackQuery().getData()
                    + " " + update.getCallbackQuery().getFrom().getId());
            return sendMessage;
        } else {
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage.setText(text + " " + update.getMessage().getChatId());
            return sendMessage;
        }
    }
}
