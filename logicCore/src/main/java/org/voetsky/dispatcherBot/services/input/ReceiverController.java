package org.voetsky.dispatcherBot.services.input;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.exceptions.ParentException.LogicCoreException;
import org.voetsky.dispatcherBot.services.input.messageValidationService.MessageValidation;
import org.voetsky.dispatcherBot.services.logic.commandHandlerService.CommandHandler;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.services.output.producerService.Producer;

@Log4j
@AllArgsConstructor
@Service
public class ReceiverController {

    private final CommandHandler commandHandler;
    private final MessageMakerService messageMakerService;
    private final MessageValidation messageValidation;
    private final Producer producer;

    public void processTextMessage(Update update) {
        try {
            if (messageValidation.isValid(update)) {
                SendMessage sendMessage = updateReceived(update);
                sendMessageToView(sendMessage);
            }
        } catch (LogicCoreException e) {
            processError(e, update);
        }
    }

    public void consumeAudioMessageUpdates(Update update) {
        try {
            if (messageValidation.isValid(update)) {
                sendMessageToView(updateReceived(update));
            }
        } catch (LogicCoreException e) {
            processError(e, update);
        }
    }

    public void consumeVoiceMessageUpdates(Update update) {
        try {
            if (messageValidation.isValid(update)) {
                sendMessageToView(updateReceived(update));
            }
        } catch (LogicCoreException e) {
            processError(e, update);
        }
    }

    public void consumeButtonUpdates(Update update) {
        try {
            if (messageValidation.isValid(update)) {
                sendMessageToView(updateReceived(update));
            }
        } catch (LogicCoreException e) {
            processError(e, update);
        }
    }

    public void processError(Exception e, Update update) {
        SendMessage s = messageMakerService.processError(e, update);
        sendMessageToView(s);
    }

    public SendMessage updateReceived(Update update) {
        return commandHandler.updateReceived(update);
    }

    public void sendMessageToView(SendMessage s) {
        producer.producerAnswer(s);
    }

}
