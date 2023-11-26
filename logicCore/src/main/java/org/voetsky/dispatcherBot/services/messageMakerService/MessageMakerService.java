package org.voetsky.dispatcherBot.services.messageMakerService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

public interface MessageMakerService {
    HashMap<Boolean, SendMessage> makeMap(Update update, String text);

    HashMap<Boolean, SendMessage> makeMap(SendMessage message);

    Long getIdFromUpdate(Update update);

    HashMap<Boolean, SendMessage> setCommandEvokeTrue(SendMessage message);

    HashMap<Boolean, SendMessage> setCommandEvokeFalse(SendMessage message);

    SendMessage makeSendMessage(Update update, String string);

}
