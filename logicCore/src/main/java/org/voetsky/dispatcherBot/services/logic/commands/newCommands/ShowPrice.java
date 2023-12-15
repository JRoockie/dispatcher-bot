package org.voetsky.dispatcherBot.services.logic.commands.newCommands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.services.repo.RepoController;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_BUTTON;
import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_TEXT;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.ADD_NUMBER;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.SHOW_PRICE;

@Log4j
@AllArgsConstructor
public class ShowPrice implements Command {
    private final String action = SHOW_PRICE.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {


        Long priceSinger = Long.parseLong(messageMakerService
                .getTextFromProperties(update, "price.singer"));
        Long priceMusic = Long.parseLong(messageMakerService
                .getTextFromProperties(update, "price.music"));
        String bill = messageMakerService
                .getTextFromProperties(update, "price.bill");
        Long discountLimit = Long.parseLong(messageMakerService
                .getTextFromProperties(update, "price.discount"));

        bill = repoController.getPrice(update, priceSinger, priceMusic, bill, discountLimit);

        String startText = messageMakerService.getTextFromProperties(
                update, "showPrice.h.m");
        String invoice = String.format(startText, bill);

        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(update);
        var msg = messageMakerService.makeSendMessage(update, invoice, markupInline);
        changeState(update, AWAITING_FOR_BUTTON);
        return msg;
    }

    @Override
    public SendMessage callback(Update update) {
        var msg = messageMakerService.makeSendMessage(update, ADD_NUMBER.toString());
        changeState(update, AWAITING_FOR_TEXT);
        return msg;
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", userState));
        }
        repoController.setUserState(update, userState);
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(Update update) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(ADD_NUMBER.toString());
        inlineKeyboardButton.setText(messageMakerService.getTextFromProperties(
                update, "Далее"));
        rowInline.add(inlineKeyboardButton);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}

