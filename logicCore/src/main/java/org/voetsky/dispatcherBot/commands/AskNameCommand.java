package org.voetsky.dispatcherBot.commands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.command.CommandInterface;
import org.voetsky.dispatcherBot.controller.RepoController;
import org.voetsky.dispatcherBot.services.messageMakerService.MessageMakerService;

import java.util.HashMap;

import static org.voetsky.dispatcherBot.UserState.*;
import static org.voetsky.dispatcherBot.commands.command.Commands.ASK_NAME_COMMAND;
import static org.voetsky.dispatcherBot.commands.command.Commands.CHOOSING_NAME_OR_ANOTHER_WAY;

@Log4j
@AllArgsConstructor
public class AskNameCommand implements CommandInterface {

    private final String action = ASK_NAME_COMMAND.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        String text = "Пожалуйста, введите ваше имя: ";
        repoController.addDefaultOrder(update);
        changeState(update, AWAITING_FOR_TEXT);

        return messageMakerService.makeSendMessage(update, text);
    }

    @Override
    public SendMessage callback(Update update) {
        repoController.setClientName(update, update.getMessage().getText());

        changeState(update, AWAITING_FOR_COMMAND);

        return messageMakerService.makeSendMessage(
                update, CHOOSING_NAME_OR_ANOTHER_WAY.toString());
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", AWAITING_FOR_BUTTON));
        }
        repoController.setUserState(update, userState);
    }

}
