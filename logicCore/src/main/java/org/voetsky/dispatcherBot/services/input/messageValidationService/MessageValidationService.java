package org.voetsky.dispatcherBot.services.input.messageValidationService;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;

public interface MessageValidationService {

    boolean isRequiredText(Update update);

    boolean isRequiredAudio(Update update);

    boolean isRequiredVoice(Update update);

    boolean isRequiredButton(Update update);

    String whichStateError(Update update);

    UserState getState(Update update);

    boolean stateCheck(Update update);

}
