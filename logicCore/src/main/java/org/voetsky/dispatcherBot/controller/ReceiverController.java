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

import java.util.HashMap;

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
            HashMap<Boolean, SendMessage> map = updateReceived(update);

            while (messageValidationService.isCommandEvoking(map)) {
                sendMessageToView(map);
                map = updateReceivedCommandEvoke(map);
            }
            sendMessageToView(messageValidationService.getSendMessageFromMap(map));

        } else {
            sendErrorMessageToView(update,
                    messageValidationService.whichStateError(update));
        }
    }

    private HashMap<Boolean, SendMessage> updateReceivedCommandEvoke(HashMap<Boolean, SendMessage> map) {
        SendMessage s = messageValidationService.getSendMessageFromMap(map);
        return commandHandler.updateReceivedCommandEvoke(s);
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
            sendErrorMessageToView(update, messageValidationService.whichStateError(update));
        }
    }

    public void consumeButtonUpdates(Update update) {
        if (messageValidationService.isRequiredButton(update)) {
            sendMessageToView(updateReceived(update));
        } else {
            sendErrorMessageToView(update, messageValidationService.whichStateError(update));
        }
    }

    public HashMap<Boolean, SendMessage> updateReceived(Update update) {
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

    public void sendMessageToView(HashMap<Boolean, SendMessage> map) {
        producerService.producerAnswer(
                messageValidationService.getSendMessageFromMap(map));
    }

    public void sendErrorMessageToView(Update update, String err) {
        SendMessage sendMessage = makeSendMessage(update, err);
        sendMessageToView(sendMessage);
    }

}
