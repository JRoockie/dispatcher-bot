package org.voetsky.dispatcherBot.services.input.messageValidationService;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;

public interface MessageValidationService {

    String whichStateError(Update update);

    UserState getState(Update update);

    boolean isValid(Update update);

    boolean throwValidationException(Update update);

    boolean isActualState(Update update);

}
