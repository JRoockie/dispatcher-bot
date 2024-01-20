package org.voetsky.dispatcherBot.services.logic.commands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.Chain;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMaker;
import org.voetsky.dispatcherBot.services.repoServices.mainRepoService.MainRepo;

import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_BUTTON;
import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_TEXT;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.ADD_NUMBER;

@Log4j
@AllArgsConstructor
public class ShowPrice implements Command, Chain {

    private final MainRepo mainRepo;
    private final MessageMaker messageMaker;

    @Override
    public SendMessage handle(Update update) {

        Long priceSinger = Long.parseLong(messageMaker
                .getTextFromProperties(update, "price.singer"));
        Long priceMusic = Long.parseLong(messageMaker
                .getTextFromProperties(update, "price.music"));
        String bill = messageMaker
                .getTextFromProperties(update, "price.bill");
        Long discountLimit = Long.parseLong(messageMaker
                .getTextFromProperties(update, "price.discount"));

        bill = mainRepo.getPrice(update,
                priceSinger,
                priceMusic,
                bill,
                discountLimit
        );

        String startText = messageMaker.getTextFromProperties(
                update, "showPrice.h.m");
        String invoice = String.format(startText, bill);

        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(update);
        var msg = messageMaker.makeSendMessage(update, invoice, markupInline, false);
        changeState(update, AWAITING_FOR_BUTTON);
        return msg;
    }

    @Override
    public SendMessage callback(Update update) {
        var msg = putNextCommand(update, ADD_NUMBER.toString());
        changeState(update, AWAITING_FOR_TEXT);
        return msg;
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", userState));
        }
        mainRepo.setUserState(update, userState);
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(Update update) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(ADD_NUMBER.toString());
        inlineKeyboardButton.setText(messageMaker.getTextFromProperties(
                update, "showPrice.b1.m"));
        rowInline.add(inlineKeyboardButton);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    @Override
    public SendMessage putNextCommand(Update update, String command) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Put command for chain evoke %S", command));
        }
        return messageMaker.makeSendMessage(update, command, false);
    }
}

