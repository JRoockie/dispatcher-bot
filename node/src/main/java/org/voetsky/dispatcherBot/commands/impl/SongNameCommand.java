package org.voetsky.dispatcherBot.commands.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.commands.CommandInterface;
import org.voetsky.dispatcherBot.controller.CommandController;


public class SongNameCommand implements CommandInterface {

    private final CommandController controller;
    private final String action;

    public SongNameCommand(String action, CommandController controller) {
        this.controller = controller;
        this.action = action;
    }


    @Override
    public SendMessage handle(Update update) {
        String text = "You have pressed the button using command in chat: ";
        //todo определять это коллбек кнопка или просто вызов комнды
        // пока при чистом вызове тут вылетает ошибка в sendCallbackMessage
        return controller.send(update,text);
    }

    @Override
    public SendMessage callback(Update update) {

        if (update.hasCallbackQuery()) {
            String text = "You have pressed the button: ";
            return controller.send(update, text);
        }
        return null;
    }
}
