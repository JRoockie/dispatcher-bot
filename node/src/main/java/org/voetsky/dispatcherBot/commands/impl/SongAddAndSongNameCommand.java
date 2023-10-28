package org.voetsky.dispatcherBot.commands.impl;


import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.CommandInterface;
import org.voetsky.dispatcherBot.controller.CommandHandler;
import org.voetsky.dispatcherBot.entity.Song;

import static org.voetsky.dispatcherBot.UserState.*;


@Log4j
public class SongAddAndSongNameCommand implements CommandInterface {

    private final String action;
    private final CommandHandler commandHandler;

    public SongAddAndSongNameCommand(String action, CommandHandler commandHandler) {
        this.action = action;
        this.commandHandler = commandHandler;
    }

    @Override
    public SendMessage handle(Update update) {
        changeState(update, AWAITING_FOR_TEXT);
        return commandHandler.send(update,"Введите название песни");
    }

    @Override
    public SendMessage callback(Update update) {
        changeState(update, AWAITING_FOR_COMMAND);

        String songName = update.getMessage().getText();
        update.getMessage().setText(songName);

        Song newSong = Song.builder()
                .songName(songName)
                .build();
        commandHandler.updateSong(update,newSong);

        return commandHandler.send(update
                ,"Название: "+update.getMessage().getText()+" принято");
    }

    @Override
    public void changeState(Update update, UserState userState) {
        log.debug("State changed to " + userState.toString());
        commandHandler.setUserState(update,userState);
    }
}
