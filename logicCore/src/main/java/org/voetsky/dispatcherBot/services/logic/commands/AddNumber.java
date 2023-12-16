package org.voetsky.dispatcherBot.services.logic.commands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.exceptions.IncorrectInputException;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.Chain;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.EditOrder;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.services.repoServices.mainRepoService.MainService;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_TEXT;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.ADD_COMMENT;

@Log4j
@AllArgsConstructor
public class AddNumber implements Command, Chain, EditOrder {
    private final MainService mainRepoService;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        String text = messageMakerService.getTextFromProperties(
                update, "addNumber.h.m");
        var msg = messageMakerService.makeSendMessage(update, text);
        changeState(update, AWAITING_FOR_TEXT);
        return msg;
    }

    @Override
    public SendMessage callback(Update update) {
        editOrder(update);
        var msg = putNextCommand(update, ADD_COMMENT.toString());
        changeState(update, AWAITING_FOR_TEXT);
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
        return messageMakerService.makeSendMessage(update, command);
    }

    @Override
    public void editOrder(Update update) {
        String data = update.getMessage().getText();
        if (isNumber(data)) {
            OrderClient orderClient = OrderClient.builder()
                    .phoneNumber(data)
                    .build();
            mainRepoService.updateOrder(update, orderClient);
            return;
        }
        throw new IncorrectInputException("Некорректный ввод, введите ваш номер");
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
