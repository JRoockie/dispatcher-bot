package org.voetsky.dispatcherBot.services.logic.commands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.Chain;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.EditSong;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMaker;
import org.voetsky.dispatcherBot.services.repoServices.mainRepoService.MainService;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_BUTTON;
import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_TEXT;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.HOW_MUCH_PEOPLE;

@Log4j
@AllArgsConstructor
public class AddLink implements Command, Chain, EditSong {
    private final MainService mainRepoService;
    private final MessageMaker messageMaker;

    @Override
    public SendMessage handle(Update update) {
        String text = messageMaker.getTextFromProperties(
                update, "addLink.h.m");
        var msg = messageMaker.makeSendMessage(update, text);
        changeState(update, AWAITING_FOR_TEXT);
        return msg;
    }

    @Override
    public SendMessage callback(Update update) {
        editSong(update);
        var msg = putNextCommand(update, HOW_MUCH_PEOPLE.toString());
        changeState(update, AWAITING_FOR_BUTTON);
        return msg;
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", userState));
        }
        mainRepoService.setUserState(update, userState);
    }

    @Override
    public SendMessage putNextCommand(Update update, String command) {
        return messageMaker.makeSendMessage(update, command);
    }

    @Override
    public void editSong(Update update) {
        String link = update.getMessage().getText();
        Song newSong = Song.builder()
                .link(link)
                .build();
        mainRepoService.updateSong(update, newSong);
    }

}
