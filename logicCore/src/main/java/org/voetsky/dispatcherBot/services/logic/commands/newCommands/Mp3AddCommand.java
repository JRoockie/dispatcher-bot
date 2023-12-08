package org.voetsky.dispatcherBot.services.logic.commands.newCommands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.logic.commands.command.Commands;
import org.voetsky.dispatcherBot.services.repo.RepoController;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;

import static org.voetsky.dispatcherBot.UserState.*;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.HOW_MUCH_PEOPLE;

@Log4j
@AllArgsConstructor
public class Mp3AddCommand implements Command {

    private final String action = Commands.MP3_ADD_COMMAND.toString();

    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        String text = messageMakerService.getTextFromProperties(
                update, "mp3AddCommand.h.m");
        var msg = messageMakerService.makeSendMessage(update, text);
        changeState(update, AWAITING_FOR_AUDIO);
        return msg;
    }

    @Override
    public SendMessage callback(Update update) {
        var msg = messageMakerService.makeSendMessage(update, HOW_MUCH_PEOPLE.toString());
        changeState(update, AWAITING_FOR_TEXT);
        repoController.addMp3(update);
       return msg;
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", userState));
        }
        repoController.setUserState(update, userState);
    }
}
