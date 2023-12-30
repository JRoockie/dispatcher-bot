package org.voetsky.dispatcherBot.services.input.messageValidationService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.exceptions.DatabaseException;
import org.voetsky.dispatcherBot.exceptions.IncorrectInputException;
import org.voetsky.dispatcherBot.exceptions.ParentException.LogicCoreException;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;
import org.voetsky.dispatcherBot.services.logic.commandHandlerService.CommandHandlerService;
import org.voetsky.dispatcherBot.services.repo.orderClientService.OrderClientRepo;
import org.voetsky.dispatcherBot.services.repo.tgUserService.TgUserRepo;

import static org.voetsky.dispatcherBot.UserState.*;


@Log4j
@AllArgsConstructor
@Service
public class MessageValidationService implements MessageValidation {

    private final CommandHandlerService commandHandlerService;
    private final TgUserRepo tgUserRepository;
    private final OrderClientRepo orderClientRepository;

    public String whichStateError(Update update) {
        UserState state = getState(update);

        switch (state) {
            case AWAITING_FOR_COMMAND:
                return "mvs.err.command.input";
            case AWAITING_FOR_TEXT:
                return "mvs.err.text.input";
            case AWAITING_FOR_BUTTON:
                return "mvs.err.button.input";
            case AWAITING_FOR_AUDIO:
                return "mvs.err.mp3.input";
            case AWAITING_FOR_VOICE:
                return "mvs.err.voice.input";
            default:
                log.error("Unknown state: " + state);
                throw new IllegalStateException(String.format("mvs.err.unknown.input" + state));
        }
    }

    public boolean isValid(Update update) {
        if (log.isDebugEnabled()) {
            log.debug("Validation...");
        }
        return isActualState(update) || throwValidationException(update);
    }

    public boolean throwValidationException(Update update) {
        String errorMessage = whichStateError(update);
        throw new IncorrectInputException(errorMessage);
    }

    public UserState getState(Update update) {
        return tgUserRepository.getState(update);
    }

    public boolean isActualState(Update update) {
        UserState state;
        try {
             state = getState(update);
        } catch (RuntimeException e){
            log.error("FATAL ERROR: ", e);
            throw new DatabaseException("mvs.err.db");
        }

        checkForInvalidOrder(update);

        if (update.getMessage() != null) {
            if (update.getMessage().getText() != null) {
                var text = update.getMessage().getText();
                var chatId = update.getMessage().getChatId().toString();
                if (text.equals("/start")) {
                    return true;
                }
                if (commandHandlerService.getActions().containsKey(text)) {
                    return AWAITING_FOR_COMMAND == state;
                } else if (commandHandlerService.getBindingBy().containsKey(chatId)) {
                    return AWAITING_FOR_TEXT == state;
                }
            } else if (update.getMessage().hasVoice()) {
                return AWAITING_FOR_VOICE == state;
            } else if (update.getMessage().hasAudio()) {
                return AWAITING_FOR_AUDIO == state;
            }
        } else if (update.hasCallbackQuery()) {
            return AWAITING_FOR_BUTTON == state;
        }
        return false;
    }

    private void checkForInvalidOrder(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("/start")) {
                tgUserRepository.setState(update, AWAITING_FOR_COMMAND);
                return;
            }
        }

        TgUser tgUser = tgUserRepository.getTgUserFromUpdate(update);
        OrderClient order = orderClientRepository.findOrderClientById(tgUser.getCurrentOrderId());
        if (order != null) {
            if (order.getDeletedWhen() != null) {
                tgUser.setUserState(INVALID_SESSION);
                throw new LogicCoreException("mvs.err.session");
            }

        }

    }

}

