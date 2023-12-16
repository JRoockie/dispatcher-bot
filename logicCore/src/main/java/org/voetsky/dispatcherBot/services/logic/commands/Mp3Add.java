package org.voetsky.dispatcherBot.services.logic.commands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.Chain;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMaker;
import org.voetsky.dispatcherBot.services.repoServices.mainRepoService.MainService;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_AUDIO;
import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_TEXT;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.HOW_MUCH_PEOPLE;

@Log4j
@AllArgsConstructor
public class Mp3Add implements Command, Chain {
    private final MainService mainRepoService;
    private final MessageMaker messageMaker;

    @Override
    public SendMessage handle(Update update) {
        String text = messageMaker.getTextFromProperties(
                update, "mp3AddCommand.h.m");
        var msg = messageMaker.makeSendMessage(update, text);
        changeState(update, AWAITING_FOR_AUDIO);
        return msg;
    }

    @Override
    public SendMessage callback(Update update) {
        var msg = putNextCommand(update, HOW_MUCH_PEOPLE.toString());
        changeState(update, AWAITING_FOR_TEXT);
        mainRepoService.addMp3(update);
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
}
