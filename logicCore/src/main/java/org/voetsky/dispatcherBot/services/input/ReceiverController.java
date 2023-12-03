package org.voetsky.dispatcherBot.services.input;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.exceptions.ParentException.LogicCoreException;
import org.voetsky.dispatcherBot.services.input.messageValidationService.MessageValidationService;
import org.voetsky.dispatcherBot.services.logic.commandHandlerService.CommandHandler;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerServiceImpl;
import org.voetsky.dispatcherBot.services.output.producerService.ProducerService;

@Log4j
@Component
@AllArgsConstructor
public class ReceiverController {

    private final CommandHandler commandHandler;
    private final MessageMakerServiceImpl messageMakerServiceImpl;
    private final MessageValidationService messageValidationService;
    private final ProducerService producerService;

    public void processTextMessage(Update update) {
        try {
            if (messageValidationService.isValid(update)) {
                SendMessage sendMessage = updateReceived(update);
                sendMessageToView(sendMessage);
            }
        } catch (LogicCoreException e) {
            log.error(e);
            sendErrorMessageToView(update, e.getMessage());
        }
    }

    public void consumeAudioMessageUpdates(Update update) {
        try {
            if (messageValidationService.isValid(update)) {
                sendMessageToView(updateReceived(update));
            }
        } catch (LogicCoreException e) {
            log.error(e);
            sendErrorMessageToView(update, e.getMessage());
        }
    }

    public void consumeVoiceMessageUpdates(Update update) {
        try {
            if (messageValidationService.isValid(update)) {
                sendMessageToView(updateReceived(update));
            }
        } catch (LogicCoreException e) {
            log.error(e);
            sendErrorMessageToView(update, e.getMessage());
        }
    }

    public void consumeButtonUpdates(Update update) {
        try {
            if (messageValidationService.isValid(update)) {
                sendMessageToView(updateReceived(update));
            }
        } catch (LogicCoreException e) {
            log.error(e);
            sendErrorMessageToView(update, e.getMessage());
        }
    }

    public SendMessage updateReceived(Update update) {
        return commandHandler.updateReceived(update);
    }

    public SendMessage makeSendMessage(Update update, String text) {
        return messageMakerServiceImpl.makeSendMessage(update, text);
    }

    public void sendMessageToView(SendMessage s) {
        producerService.producerAnswer(s);
    }

    public void sendMessageToView(Update update, String text) {
        producerService.producerAnswer(makeSendMessage(update, text));
    }

    public void sendErrorMessageToView(Update update, String err) {
        SendMessage sendMessage = makeSendMessage(update, err);
        sendMessageToView(sendMessage);
    }

}
