package org.voetsky.dispatcherBot.commands;

import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.interf.CommandInterface;
import org.voetsky.dispatcherBot.controller.CommandHandler;
import static org.voetsky.dispatcherBot.UserState.*;
import static org.voetsky.dispatcherBot.commands.Commands.CHOOSING_NAME_OR_ANOTHER_WAY;

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
        commandHandler.getBigDaoService().setClientName(update,update.getMessage().getText());
        changeState(update, AWAITING_FOR_COMMAND);
        update.getMessage().setText(CHOOSING_NAME_OR_ANOTHER_WAY.toString());
        return commandHandler.updateReceiver(update);
    }

    @Override
    public void changeState(Update update, UserState userState) {
        log.debug("State changed to " + userState.toString());
        commandHandler.setUserState(update,userState);
    }
}
