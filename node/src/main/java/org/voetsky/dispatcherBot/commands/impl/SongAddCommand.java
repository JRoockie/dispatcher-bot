package org.voetsky.dispatcherBot.commands.impl;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.commands.CommandInterface;
import org.voetsky.dispatcherBot.controller.CommandController;

public class SongAddCommand implements CommandInterface {

    private final String action;
    private final CommandController controller;

    public SongAddCommand(String action, CommandController controller) {
        this.action = action;
        this.controller = controller;
    }


    @Override
    public SendMessage handle(Update update) {

        return new SendMessage();
    }

    @Override
    public SendMessage callback(Update update) {
        return handle(update);
    }
}
