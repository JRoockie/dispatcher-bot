package org.voetsky.dispatcherBot.testClasses.testCommands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;

import java.util.HashMap;

public interface TestCommands {

    HashMap<Boolean, SendMessage> handle(Update update);

    HashMap<Boolean, SendMessage> callback(Update update);

    void changeState(Update update, UserState userState);

    boolean isChain();

    boolean isCallback();

    String getState();
}
