package org.voetsky.dispatcherBot.services.logic.commands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.InlineKeyboard;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMaker;
import org.voetsky.dispatcherBot.services.repoServices.mainRepoService.MainService;

import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_BUTTON;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.CLIENT_NAME;

@Log4j
@AllArgsConstructor
public class Start implements Command, InlineKeyboard {
    private final MainService mainRepoService;
    private final MessageMaker messageMaker;

    @Override
    public SendMessage handle(Update update) {
        String text = messageMaker.getTextFromProperties(
                update, "startCommand.h.m");
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(update);
        var msg = messageMaker.makeSendMessage(update, text, markupInline);
        changeState(update, AWAITING_FOR_BUTTON);
        return msg;
    }

    @Override
    public SendMessage callback(Update update) {
        return handle(update);
    }

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup(Update update) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(CLIENT_NAME.toString());
        inlineKeyboardButton.setText(messageMaker.getTextFromProperties(
                update, "startCommand.b1.m"));
        rowInline.add(inlineKeyboardButton);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", userState));
        }
        mainRepoService.setUserState(update, userState);
    }

}
