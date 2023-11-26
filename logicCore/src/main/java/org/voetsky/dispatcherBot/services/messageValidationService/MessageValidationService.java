package org.voetsky.dispatcherBot.services.messageValidationService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;

import java.util.HashMap;

public interface MessageValidationService {

    boolean isRequiredText(Update update);

    boolean isRequiredAudio(Update update);

    boolean isRequiredVoice(Update update);

    boolean isRequiredButton(Update update);

    String whichStateError(Update update);

    UserState getState(Update update);

    boolean stateCheck(Update update);

    public boolean isCommandEvoking(HashMap<Boolean, SendMessage> map);

    public SendMessage getSendMessageFromMap(HashMap<Boolean, SendMessage> map);
}
