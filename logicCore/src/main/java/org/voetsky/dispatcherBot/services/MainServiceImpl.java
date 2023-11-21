package org.voetsky.dispatcherBot.services;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.controller.CommandHandler;
import org.voetsky.dispatcherBot.entity.TgUser;
import org.voetsky.dispatcherBot.services.interf.MainService;

import static org.voetsky.dispatcherBot.UserState.*;

@Service
@Log4j
public class MainServiceImpl implements MainService {

    private final CommandHandler commandHandler;
    private final ProducerServiceImpl producerService;
    private final MessageServiceImpl messageService;

    public MainServiceImpl(CommandHandler commandHandler, ProducerServiceImpl producerService, MessageServiceImpl messageService) {
        this.commandHandler = commandHandler;
        this.producerService = producerService;
        this.messageService = messageService;
    }

    @Override
    public void processTextMessage(Update update) {
        log.debug("NODE: Text message is received");

        var textMessage = update.getMessage();
        var telegramUser = textMessage.getFrom();
//        var user = commandHandler.findOrSaveAppUser(telegramUser);
        if (stateCheck(update)) {
            log.debug("NODE: Sending message to controller");
            producerService.producerAnswer(commandHandler.updateReceiver(update));
        } else {
            errorSender(update);
        }
    }

    public void consumeAudioMessageUpdates(Update update) {
        log.debug("NODE: Mp3 message received");

        if (stateCheck(update)) {
//            producerService.producerAnswer(messageService.send(update, "Audio message is received from NODE"));
            producerService.producerAnswer(commandHandler.updateReceiver(update));
        } else {
            errorSender(update);
        }
    }

    public void consumeVoiceMessageUpdates(Update update) {
        log.debug("NODE: Voice message is received");
        if (stateCheck(update)) {
            producerService.producerAnswer(commandHandler.updateReceiver(update));
        } else {
            errorSender(update);
        }
    }

    public void consumeButtonUpdates(Update update) {
        log.debug("NODE: Button message is received");
        if (stateCheck(update)) {
            producerService.producerAnswer(commandHandler.updateReceiver(update));
        } else {
            errorSender(update);
        }
    }

    public void errorSender(Update update) {
        try {
            String error = whichStateError(update);
            producerService.producerAnswer(messageService.send(update, error));
        } catch (Exception e) {
            commandHandler.send(update, ". Введите /start");
        }
    }

    public String whichStateError(Update update) {
        String string = "Ошибка! Ожидается ";
        UserState s = getState(update);
        if (s.equals(AWAITING_FOR_COMMAND)) {
            return string + "ввод команды";
        } else if (s.equals(AWAITING_FOR_TEXT)) {
            return string + "ввод текста";
        } else if (s.equals(AWAITING_FOR_BUTTON)) {
            return string + "нажатие кнопки";
        } else if (s.equals(AWAITING_FOR_AUDIO)) {
            return string + "mp3 файл";
        } else if (s.equals(AWAITING_FOR_VOICE)) {
            return string + "голосовое сообщение";
        }
        return "Неизвестная ошибка";
    }

    public UserState getState(Update update) {

        UserState state;
        TgUser tgUser;

        if (update.hasCallbackQuery()) {
            tgUser = commandHandler.findOrSaveAppUser(update.getCallbackQuery().getFrom());
            state = tgUser.getUserState();
        } else {
            tgUser = commandHandler.findOrSaveAppUser(update.getMessage().getFrom());
            state = tgUser.getUserState();
        }
        return state;
    }

    public boolean stateCheck(Update update) {

        UserState state = getState(update);
        if (update.getMessage() != null) {
            if (update.getMessage().getText() != null) {
                log.debug("VALID: Choosing command ");
                var key = update.getMessage().getText();
                var chatId = update.getMessage().getChatId().toString();

                if (commandHandler.getActions().containsKey(key)) {
                    log.debug("VALID: It is COMMAND " + commandHandler.getActions().get(key));
                    return AWAITING_FOR_COMMAND.equals(state);

                } else if (commandHandler.getBindingBy().containsKey(chatId)) {
                    log.debug("VALID: It is text for CALLBACK part of : " + commandHandler.getBindingBy().get(chatId));
                    return AWAITING_FOR_TEXT.equals(state);
                }
            } else if (update.getMessage().hasVoice()) {
                log.debug("VALID: It is VOICE ");
                return AWAITING_FOR_VOICE.equals(state);

            } else if (update.getMessage().hasAudio()) {
                log.debug("VALID: It is mp3 ");
                return AWAITING_FOR_AUDIO.equals(state);
            }
        } else if (update.hasCallbackQuery()) {
            log.debug("VALID: It is BUTTON ");
            return AWAITING_FOR_BUTTON.equals(state);

        } else {
            log.debug("VALID: Callback not found, command not found ");
            return false;
        }
        return false;
    }

}
