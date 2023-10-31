package org.voetsky.dispatcherBot.commands.impl;

import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.CommandInterface;
import org.voetsky.dispatcherBot.controller.CommandHandler;

import static org.voetsky.dispatcherBot.UserState.*;

@Log4j
public class Mp3AddCommand implements CommandInterface {

    private final String action;
    private final CommandHandler commandHandler;

    public Mp3AddCommand(String action, CommandHandler commandHandler) {
        this.action = action;
        this.commandHandler = commandHandler;
    }

    @Override
    public SendMessage handle(Update update) {
        changeState(update, AWAITING_FOR_AUDIO);
        return commandHandler.send(update,"Отправьте песню mp3 файлом");
    }

    @Override
    public SendMessage callback(Update update) {
        changeState(update, AWAITING_FOR_COMMAND);
        commandHandler.addMp3(update);
       return commandHandler.send(update, "Успешное добавление аудио в бд");
    }

    @Override
    public void changeState(Update update, UserState userState) {
        log.debug("State changed to " + userState.toString());
        commandHandler.setUserState(update,userState);
    }
}
