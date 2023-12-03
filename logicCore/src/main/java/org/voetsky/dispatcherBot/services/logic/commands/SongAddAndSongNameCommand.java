package org.voetsky.dispatcherBot.services.logic.commands;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.logic.commands.command.CommandInterface;
import org.voetsky.dispatcherBot.services.logic.commands.command.Commands;
import org.voetsky.dispatcherBot.services.repo.RepoController;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;

import static org.voetsky.dispatcherBot.UserState.*;


@Log4j
@AllArgsConstructor
public class SongAddAndSongNameCommand implements CommandInterface {

    private final String action = Commands.SONG_ADD_AND_ADD_SONG_NAME_COMMAND.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        String text = "Введите название песни";

        changeState(update, AWAITING_FOR_TEXT);
        return messageMakerService.makeSendMessage(update, text);
    }

    @Override
    public SendMessage callback(Update update) {
        String text = String.format("Название: %s принято", update.getMessage().getText());
        changeState(update, AWAITING_FOR_COMMAND);

        String songName = update.getMessage().getText();
//        update.getMessage().setText(songName);

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
