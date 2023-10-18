package org.voetsky.dispatcherBot.services.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.controller.CommandHandler;
import org.voetsky.dispatcherBot.dao.TgUserDao;
import org.voetsky.dispatcherBot.dao.OrderClientDao;
import org.voetsky.dispatcherBot.dao.SongDao;
import org.voetsky.dispatcherBot.services.MainService;

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
            errorSender(update, "textError, write /start");
        }
    }

    public void consumeAudioMessageUpdates(Update update) {
        log.debug("NODE: Mp3 message is received");

        if (stateCheck(update)) {
            producerService.producerAnswer(messageService.send(update, "Audio message is received from NODE"));
        } else {
            errorSender(update, "mp3Error");
        }
    }

    public void consumeVoiceMessageUpdates(Update update) {
        log.debug("NODE: Voice message is received");
        if (stateCheck(update)) {
            producerService.producerAnswer(messageService.send(update, "Voice message is received from NODE"));
        } else {
            errorSender(update, "voiceError");
        }
    }

    public void consumeButtonUpdates(Update update) {
        log.debug("NODE: Button message is received");
        if (stateCheck(update)) {
            producerService.producerAnswer(commandHandler.updateReceiver(update));
        } else {
            errorSender(update, "buttonError");
        }
    }

    public void errorSender(Update update, String s) {
        String error = "Некорректный state. Текущий state: " + getState(update);
        producerService.producerAnswer(messageService.send(update, s + " " + error));
    }

    public UserState getState(Update update) {
        UserState state;
        if (update.hasCallbackQuery()) {
            state = commandHandler.findAppUsersByTelegramUserId(
                    update.getCallbackQuery().getFrom().getId()).getUserState();
        } else {
            var telegramUser = update.getMessage().getFrom();
            state = commandHandler.findAppUsersByTelegramUserId(telegramUser.getId()).getUserState();
        }
        return state;
    }

    public boolean stateCheck(Update update) {
// 2 state: awaiting for text, command state, awaiting for doc, awaiting for mp3,
        // в стейте awaiting нельзя ввести команду и запустится предыдущая. В стейте command
        // команда вводится но нельзя ввести кастом ввод, запуск пред команды
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
