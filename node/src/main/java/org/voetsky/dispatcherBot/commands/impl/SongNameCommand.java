package org.voetsky.dispatcherBot.commands.impl;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.CommandInterface;
import org.voetsky.dispatcherBot.controller.CommandHandler;

import static org.voetsky.dispatcherBot.UserState.*;

@Log4j
public class SongNameCommand implements CommandInterface {

    private final CommandHandler commandHandler;
    private final String action;

    public SongNameCommand(String action, CommandHandler controller) {
        this.commandHandler = controller;
        this.action = action;
    }


    @Override
    public SendMessage handle(Update update) {
        String text = "You have executed the command in chat: ";
        //todo определять это коллбек кнопка или просто вызов комнды
        // пока при чистом вызове тут вылетает ошибка в sendCallbackMessage
        changeState(update, AWAITING_FOR_COMMAND);
        return commandHandler.send(update, text);
    }

    @Override
    public SendMessage callback(Update update) {
        //todo  добавить обработку ввода. Чтоби вводом был именно текст
        String text = "Callback has received: \""
                + update.getMessage().getText()
                + "\" " + action + " UserId:";
        changeState(update, AWAITING_FOR_COMMAND);
        return commandHandler.send(update, text);

    }

    @Override
    public void changeState(Update update, UserState userState) {
        log.debug("State changed to " + AWAITING_FOR_COMMAND);
        commandHandler.setUserState(commandHandler.getTelegramUserIdFromUpdate(update), AWAITING_FOR_COMMAND);
    }
}
