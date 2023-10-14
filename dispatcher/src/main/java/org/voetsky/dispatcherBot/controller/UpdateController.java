package org.voetsky.dispatcherBot.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.service.UpdateProducer;
import org.voetsky.dispatcherBot.utils.MessageUtils;

import static org.voetsky.model.RabbitQueue.*;

@Component
@Log4j
public class UpdateController {

    private TelegramBot telegramBot;
    private final UpdateProducer updateProducer;
    private final MessageUtils messageUtils;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {

        if (update.getMessage() != null) {
            distributeMessagesByType(update);
        } else if (update.hasCallbackQuery()){
            distributeMessagesByType(update);
        }else {
            log.error("Unsupported message type is received: " + update);
        }

    }

    private void distributeMessagesByType(Update update) {

        var message = update.getMessage();
        if (update.hasCallbackQuery()) {
            processButton(update);
        } else if (message.hasDocument()) {
            processDocMessage(update);
        } else if (message.hasVoice()) {
            processVoiceMessage(update);
        } else if (message.hasText()) {
            processTextMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Неподдерживаемый тип сообщения!");
        setView(sendMessage);
    }

    private void setFileIsReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Обработка...");
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processVoiceMessage(Update update) {
        setFileIsReceivedView(update);
        updateProducer.produce(VOICE_MESSAGE_UPDATE, update);
    }

    private void processDocMessage(Update update) {
        setFileIsReceivedView(update);
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void processButton(Update update) {
        updateProducer.produce(BUTTON_UPDATE, update);
    }


}
