package org.voetsky.dispatcherBot.commands.impl;

import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.CommandInterface;
import org.voetsky.dispatcherBot.controller.CommandHandler;
import static org.voetsky.dispatcherBot.UserState.*;
@Log4j
public class AskNameCommand implements CommandInterface {

    private final String action;
    private final CommandHandler commandHandler;

    public AskNameCommand(String action, CommandHandler commandHandler) {
        this.action = action;
        this.commandHandler = commandHandler;
    }

    @Override
    public SendMessage handle(Update update) {
        String text = "Пожалуйста, введите ваше имя: ";
        changeState(update, AWAITING_FOR_TEXT);
        return commandHandler.send(update,text);
    }

    @Override
    public SendMessage callback(Update update) {
        changeState(update, AWAITING_FOR_COMMAND);
        String command ="/choosingNameOrAnotherWay";
        update.getMessage().setText(command);
        return commandHandler.updateReceiver(update);
    }

    @Override
    public void changeState(Update update, UserState userState) {
        log.debug("State changed to " + userState.toString());
        commandHandler.setUserState(commandHandler.getTelegramUserIdFromUpdate(update), userState);
    }
}
