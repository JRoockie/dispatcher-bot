package org.voetsky.dispatcherBot.commands;

import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.interf.CommandInterface;
import org.voetsky.dispatcherBot.controller.CommandHandler;

import static org.voetsky.dispatcherBot.UserState.*;

@Log4j
public class VoiceAddCommand implements CommandInterface {

    private final String action;
    private final CommandHandler commandHandler;

    public VoiceAddCommand(String action, CommandHandler commandHandler) {
        this.action = action;
        this.commandHandler = commandHandler;
    }

    @Override
    public SendMessage handle(Update update) {
        changeState(update, AWAITING_FOR_VOICE);
        return commandHandler.send(update,"Отправьте голосовое сообщение");
    }

    @Override
    public SendMessage callback(Update update) {
        changeState(update, AWAITING_FOR_COMMAND);
        commandHandler.addVoice(update);
        return commandHandler.send(update, "Успешное добавление ГС в бд");
    }

    @Override
    public void changeState(Update update, UserState userState) {
        log.debug("State changed to " + userState.toString());
        commandHandler.setUserState(update,userState);
    }
}
