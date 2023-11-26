package org.voetsky.dispatcherBot.testClasses.testCommands;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;

import java.util.HashMap;

@Getter
@Component
public class Start implements TestCommands {

    private final boolean chain = false;
    private final String state = "dfefefef";
    private final boolean callback = false;

    @Override
    public HashMap<Boolean, SendMessage> handle(Update update) {
        return null;
    }

    @Override
    public HashMap<Boolean, SendMessage> callback(Update update) {
        return null;
    }

    @Override
    public void changeState(Update update, UserState userState) {

    }
}
