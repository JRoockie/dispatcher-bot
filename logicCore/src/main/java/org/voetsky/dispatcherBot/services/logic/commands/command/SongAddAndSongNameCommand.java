package org.voetsky.dispatcherBot.services.logic.commands.command;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.logic.commands.command.Commands;
import org.voetsky.dispatcherBot.services.repo.RepoController;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;

import static org.voetsky.dispatcherBot.UserState.*;


@Log4j
@AllArgsConstructor
public class SongAddAndSongNameCommand implements Command {

    private final String action = Commands.SONG_ADD_AND_ADD_SONG_NAME_COMMAND.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        String text = messageMakerService.getTextFromProperties(
                update, "songAddAndSongNameCommand.h.m");
        changeState(update, AWAITING_FOR_TEXT);
        return messageMakerService.makeSendMessage(update, text);
    }

    @Override
    public SendMessage callback(Update update) {
        String text = String.format(
                messageMakerService.getTextFromProperties(
                        update,"songAddAndSongNameCommand.c.m"),
                update.getMessage().getText());

        changeState(update, AWAITING_FOR_COMMAND);
        String songName = update.getMessage().getText();
        Song newSong = Song.builder()
                .songName(songName)
                .build();
        repoController.addSong(update, newSong);

        return messageMakerService.makeSendMessage(update, text);
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", AWAITING_FOR_BUTTON));
        }
        repoController.setUserState(update, userState);
    }
}