package org.voetsky.dispatcherBot.services.logic.commands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.Chain;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.EditUser;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMaker;
import org.voetsky.dispatcherBot.services.repoServices.mainRepoService.MainRepo;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_BUTTON;
import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_TEXT;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.SONG_NAME_OR_MP3;

@Log4j
@AllArgsConstructor
public class ClientName implements Command, Chain, EditUser {

    private final MainRepo mainRepo;
    private final MessageMaker messageMaker;

    @Override
    public SendMessage handle(Update update) {
        String text = messageMaker.getTextFromProperties(
                update, "clientNameCommand.c.m");
        var msg = messageMaker.makeSendMessage(update, text, false);
        changeState(update, AWAITING_FOR_TEXT);
        return msg;
    }

    @Override
    public SendMessage callback(Update update) {
        editTgUser(update);
        var msg = putNextCommand(update, SONG_NAME_OR_MP3.toString());
        changeState(update, AWAITING_FOR_BUTTON);
        return msg;
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", userState));
        }
        mainRepo.setUserState(update, userState);
    }

    @Override
    public SendMessage putNextCommand(Update update, String command) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Put command for chain evoke %S", command));
        }
        return messageMaker.makeSendMessage(update, command, false);
    }

    @Override
    public void editTgUser(Update update) {
        String name = update.getMessage().getText();
        if (!isNumber(name)) {
            OrderClient orderClient = OrderClient.builder()
                    .nameAsClient(name)
                    .build();
            mainRepo.updateOrder(update, orderClient);
        }
    }

    private boolean isNumber(String data) {
        try {
            Long checkNumber = Long.valueOf(data);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
