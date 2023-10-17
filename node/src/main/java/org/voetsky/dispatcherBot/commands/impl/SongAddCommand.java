package org.voetsky.dispatcherBot.commands.impl;


import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.CommandInterface;
import org.voetsky.dispatcherBot.controller.CommandHandler;

import static org.voetsky.dispatcherBot.UserState.*;


@Log4j
public class SongAddCommand implements CommandInterface {

    private final String action;
    private final CommandHandler commandHandler;

    public SongAddCommand(String action, CommandHandler controller) {
        this.action = action;
        this.commandHandler = controller;
    }


    @Override
    public SendMessage handle(Update update) {
        changeState(update, AWAITING_FOR_TEXT);
        return commandHandler.send(update,"Введите название песни");
    }

    @Override
    public SendMessage callback(Update update) {
        changeState(update, AWAITING_FOR_COMMAND);
        return commandHandler.send(update
                ,"Песня: "+update.getMessage().getText()+" принята");
    }

    @Override
    public void changeState(Update update, UserState userState) {
        commandHandler.setUserState(update.getMessage().getFrom(), userState);
        log.debug("State changed to " + userState.toString());
    }
}
