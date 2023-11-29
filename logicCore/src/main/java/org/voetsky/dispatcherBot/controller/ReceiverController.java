package org.voetsky.dispatcherBot.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.services.commandHandlerService.CommandHandler;
import org.voetsky.dispatcherBot.services.messageMakerService.MessageMakerServiceImpl;
import org.voetsky.dispatcherBot.services.messageValidationService.MessageValidationService;
import org.voetsky.dispatcherBot.services.producerService.ProducerService;

@Log4j
@Component
@AllArgsConstructor
public class ReceiverController {

    private final CommandHandler commandHandler;
    private final MessageMakerServiceImpl messageMakerServiceImpl;
    private final MessageValidationService messageValidationService;
    private final ProducerService producerService;

    public void processTextMessage(Update update) {
        if (messageValidationService.isRequiredText(update)) {
            SendMessage sendMessage = updateReceived(update);
            sendMessageToView(sendMessage);
        } else {
            sendErrorMessageToView(update,
                    messageValidationService.whichStateError(update));
        }
    }

    public void consumeAudioMessageUpdates(Update update) {
        if (messageValidationService.isRequiredAudio(update)) {
            sendMessageToView(updateReceived(update));
        } else {
            sendErrorMessageToView(update,
                    messageValidationService.whichStateError(update));
        }
    }

    public void consumeVoiceMessageUpdates(Update update) {
        if (messageValidationService.isRequiredVoice(update)) {
            sendMessageToView(updateReceived(update));
        } else {
            sendErrorMessageToView(update,
                    messageValidationService.whichStateError(update));
        }
    }

    public void consumeButtonUpdates(Update update) {
        if (messageValidationService.isRequiredButton(update)) {
            sendMessageToView(updateReceived(update));
        } else {
            sendErrorMessageToView(update,
                    messageValidationService.whichStateError(update));
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
