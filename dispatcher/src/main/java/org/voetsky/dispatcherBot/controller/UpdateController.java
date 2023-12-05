package org.voetsky.dispatcherBot.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.service.output.updateProducer.UpdateProducer;
import org.voetsky.dispatcherBot.service.messageutils.MakeMessage;

import static org.voetsky.model.RabbitQueue.*;


@Log4j
@Component
public class UpdateController {

    private TelegramBot telegramBot;
    private final UpdateProducer updateProducer;
    private final MakeMessage makeMessage;

    public UpdateController(UpdateProducer updateProducer, MakeMessage makeMessage) {
        this.updateProducer = updateProducer;
        this.makeMessage = makeMessage;
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
            if (log.isDebugEnabled()){
                log.error("Unsupported message type is received: " + update);
            }
        }

    }

    private void distributeMessagesByType(Update update) {
        var message =update.getMessage();

        if (update.hasCallbackQuery()) {
            processButton(update);
        } else if (message.hasAudio()) {
            processAudioMessage(update);
        } else if (message.hasVoice()) {
            processVoiceMessage(update);
        } else if (message.hasText()) {
            processTextMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = makeMessage.generateSendMessageWithText(update,
                "Неподдерживаемый тип сообщения!");
        setView(sendMessage);
    }

    private void setFileIsReceivedView(Update update) {
        var sendMessage = makeMessage.generateSendMessageWithText(update,
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

    private void processAudioMessage(Update update) {
        setFileIsReceivedView(update);
        updateProducer.produce(AUDIO_MESSAGE_UPDATE, update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void processButton(Update update) {
        updateProducer.produce(BUTTON_UPDATE, update);
    }
}
