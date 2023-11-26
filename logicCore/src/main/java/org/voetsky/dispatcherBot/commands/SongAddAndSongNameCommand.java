package org.voetsky.dispatcherBot.commands;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.command.CommandInterface;
import org.voetsky.dispatcherBot.controller.RepoController;
import org.voetsky.dispatcherBot.entity.Song;
import org.voetsky.dispatcherBot.services.messageMakerService.MessageMakerService;

import java.util.HashMap;

import static org.voetsky.dispatcherBot.UserState.*;
import static org.voetsky.dispatcherBot.commands.command.Commands.SONG_ADD_AND_ADD_SONG_NAME_COMMAND;


@Log4j
@AllArgsConstructor
public class SongAddAndSongNameCommand implements CommandInterface {

    private final String action = SONG_ADD_AND_ADD_SONG_NAME_COMMAND.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public HashMap<Boolean, SendMessage> handle(Update update) {
        changeState(update, AWAITING_FOR_TEXT);
        return messageMakerService.makeMap(update, "Введите название песни");
    }

    @Override
    public HashMap<Boolean, SendMessage> callback(Update update) {
        changeState(update, AWAITING_FOR_COMMAND);

        String songName = update.getMessage().getText();
        update.getMessage().setText(songName);

        Song newSong = Song.builder()
                .songName(songName)
                .build();
        repoController.updateSong(update,newSong);

        return messageMakerService.makeMap(update,
                String.format("Название: %s принято", update.getMessage().getText()));
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", AWAITING_FOR_BUTTON));
        }
        repoController.setUserState(update,userState);
    }
}
