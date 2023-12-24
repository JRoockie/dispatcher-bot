package org.voetsky.dispatcherBot.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.configuration.RabbitConfiguration;
import org.voetsky.dispatcherBot.service.messageutils.MakeMessage;
import org.voetsky.dispatcherBot.service.output.updateProducer.UpdateProducer;

@Log4j
@Component
public class UpdateController {

    private TelegramBot telegramBot;
    private final UpdateProducer updateProducer;
    private final MakeMessage makeMessage;
    private final RabbitConfiguration rabbitConfiguration;

    public UpdateController(UpdateProducer updateProducer, MakeMessage makeMessage, RabbitConfiguration rabbitConfiguration) {
        this.updateProducer = updateProducer;
        this.makeMessage = makeMessage;
        this.rabbitConfiguration = rabbitConfiguration;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update.getMessage() != null) {
            distributeMessagesByType(update);
        } else if (update.hasCallbackQuery()) {
            distributeMessagesByType(update);
        } else {
            if (log.isDebugEnabled()) {
                log.error(String.format("Unsupported message type is received: %s", update));
            }
            setUnsupportedMessageTypeView(update);
        }
    }

    public void distributeMessagesByType(Update update) {
        var message = update.getMessage();
        loggingUpdate(update);
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

    public void loggingUpdate(Update update) {
        if (log.isDebugEnabled()) {
        var message = update.getMessage();
            if (update.hasCallbackQuery()) {
                log.debug(String.format("Button input %s", update.getCallbackQuery().getData()));
            } else if (message.hasAudio()) {
                log.debug(String.format("Mp3 input %s", update.getMessage().getAudio().getFileId()));
            } else if (message.hasVoice()) {
                log.debug(String.format("Voice input %s", update.getMessage().getVoice().getFileId()));
            } else if (message.hasText()) {
                log.debug(String.format("Text input %s", update.getMessage().getText()));
            } else {
                log.debug(String.format("Text input %s", update.getUpdateId()));
            }
        }
    }

    public void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = makeMessage.generateSendMessageWithText(update,
                "type.error", true);
        setView(sendMessage);
    }

    public void setFileIsReceivedView(Update update) {
        var sendMessage = makeMessage.generateSendMessageWithText(update,
                "process", true);
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processVoiceMessage(Update update) {
        setFileIsReceivedView(update);
        updateProducer.produce(rabbitConfiguration.getVoiceMessageUpdateQueue(), update);
    }

    private void processAudioMessage(Update update) {
        setFileIsReceivedView(update);
        updateProducer.produce(rabbitConfiguration.getAudioMessageUpdateQueue(), update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(rabbitConfiguration.getTextMessageUpdateQueue(), update);
    }

    private void processButton(Update update) {
        updateProducer.produce(rabbitConfiguration.getButtonMessageQueue(), update);
    }
}
