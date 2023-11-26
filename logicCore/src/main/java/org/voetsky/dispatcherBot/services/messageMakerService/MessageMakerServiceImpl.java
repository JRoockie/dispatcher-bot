package org.voetsky.dispatcherBot.services.messageMakerService;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

@Log4j
@Service
public class MessageMakerServiceImpl implements MessageMakerService {

    @Override
    public HashMap<Boolean, SendMessage> makeMap(Update update, String text) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(getIdFromUpdate(update));
            sendMessage.setText(text);
            return makeMap(sendMessage);
        } catch (Exception e) {
            SendMessage sendMessage = new SendMessage("874396856", "Ошибка");
            return makeMap(sendMessage);
        }
    }

    @Override
    public Long getIdFromUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        } else {
            return update.getMessage().getChatId();
        }
    }

    @Override
    public HashMap<Boolean, SendMessage> setCommandEvokeTrue(SendMessage message) {
        HashMap<Boolean, SendMessage> map = new HashMap<>();
        map.put(true, message);
        if (log.isDebugEnabled()) {
            log.debug(String.format("Command chain is: %s", true));
        }
        return map;
    }

    @Override
    public HashMap<Boolean, SendMessage> setCommandEvokeFalse(SendMessage message) {
        HashMap<Boolean, SendMessage> map = new HashMap<>();
        map.put(false, message);
        if (log.isDebugEnabled()) {
            log.debug(String.format("Command chain is: %s", false));
        }
        return map;
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
            SendMessage sendMessage = new SendMessage("874396856", "Ошибка");
            return sendMessage;
        }
    }

        @Override
        public HashMap<Boolean, SendMessage> makeMap (SendMessage message){
            HashMap<Boolean, SendMessage> map = new HashMap<>();
            map.put(false, message);
            return map;
        }


    }
