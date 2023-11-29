package org.voetsky.dispatcherBot.services.messageValidationService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.commandHandlerService.CommandHandlerService;
import org.voetsky.dispatcherBot.services.repoService.tgUserService.TgUserRepositoryService;

import java.util.HashMap;

import static org.voetsky.dispatcherBot.UserState.*;

@AllArgsConstructor
@Log4j
@Service
public class MessageValidationServiceImpl implements MessageValidationService {

    private final CommandHandlerService commandHandlerService;
    private final TgUserRepositoryService tgUserRepositoryService;

    @Override
    public boolean isRequiredText(Update update) {
        log.debug("NODE: Text message is received");
        var textMessage = update.getMessage();
        var telegramUser = textMessage.getFrom();
//        var user = commandHandler.findOrSaveAppUser(telegramUser);
        return stateCheck(update);
    }

    public boolean isRequiredAudio(Update update) {
        log.debug("NODE: Mp3 message received");
        return stateCheck(update);
    }

    public boolean isRequiredVoice(Update update) {
        log.debug("NODE: Voice message is received");
        return stateCheck(update);
    }

    public boolean isRequiredButton(Update update) {
        log.debug("NODE: Button message is received");
        return stateCheck(update);
    }

    public String whichStateError(Update update) {
        String string = "Ошибка! Ожидается";
        UserState s = getState(update);
        if (s.equals(AWAITING_FOR_COMMAND)) {
            return string + " ввод команды";
        } else if (s.equals(AWAITING_FOR_TEXT)) {
            return string + " ввод текста";
        } else if (s.equals(AWAITING_FOR_BUTTON)) {
            return string + " нажатие кнопки";
        } else if (s.equals(AWAITING_FOR_AUDIO)) {
            return string + " mp3 файл";
        } else if (s.equals(AWAITING_FOR_VOICE)) {
            return string + " голосовое сообщение";
        }
        return "Неизвестная ошибка";
    }
//todo определить где все таки делать вызов к бд.
// В CoreControllere или  влюбом классе вшивать зависимость на неободимую бд

    public UserState getState(Update update) {
        return tgUserRepositoryService.getState(update);
    }

    public boolean stateCheck(Update update) {

        UserState state = getState(update);
        if (update.getMessage() != null) {
            if (update.getMessage().getText() != null) {
                log.debug("VALID: Choosing command ");
                var key = update.getMessage().getText();
                var chatId = update.getMessage().getChatId().toString();

                if (commandHandlerService.getActions().containsKey(key)) {
                    log.debug("VALID: It is COMMAND " + commandHandlerService.getActions().get(key));
                    return AWAITING_FOR_COMMAND.equals(state);

                } else if (commandHandlerService.getBindingBy().containsKey(chatId)) {
                    log.debug("VALID: It is text for CALLBACK part of : " + commandHandlerService.getBindingBy().get(chatId));
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
