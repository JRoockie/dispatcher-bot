package org.voetsky.dispatcherBot.services.logic.commands.newCommands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.services.repo.RepoController;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_BUTTON;
import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_TEXT;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.*;

@Log4j
@AllArgsConstructor
public class ClientNameCommand implements Command {

    private final String action = CLIENT_NAME_COMMAND.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        String text = messageMakerService.getTextFromProperties(
                update, "clientNameCommand.c.m");
        repoController.addOrder(update);
        var msg = messageMakerService.makeSendMessage(update, text);
        changeState(update, AWAITING_FOR_TEXT);
        return msg;
    }

    @Override
    public SendMessage callback(Update update) {
        String name = update.getMessage().getText();
        TgUser tgUser = TgUser.builder()
                .nameAsClient(name)
                .build();
        repoController.updateUser(update, tgUser);
        var msg = messageMakerService.makeSendMessage(update, SONG_NAME_OR_MP3.toString());
        changeState(update, AWAITING_FOR_BUTTON);
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