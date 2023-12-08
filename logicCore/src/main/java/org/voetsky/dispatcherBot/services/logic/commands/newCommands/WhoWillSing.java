package org.voetsky.dispatcherBot.services.logic.commands.newCommands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.services.repo.RepoController;

import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_BUTTON;
import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_VOICE;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.*;

@Log4j
@AllArgsConstructor
public class WhoWillSing implements Command {
    private final String action = WHO_WILL_SING.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        String text = messageMakerService.getTextFromProperties(
                update, "whoWillSing.h.m");
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(update);
        var msg = messageMakerService.makeSendMessage(update, text, markupInline);
        changeState(update, AWAITING_FOR_BUTTON);
        return msg;
    }

    @Override
    public SendMessage callback(Update update) {
        String callbackMessage = update.getCallbackQuery().getMessage().getText();
//       todo repoController.addWhoWillSong();
        //if it will be child - ask everytime, if adult - only one
        var msg = messageMakerService.makeSendMessage(
                update, VOICE_ADD_COMMAND.toString());
        changeState(update, AWAITING_FOR_VOICE);
        return msg;

    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(Update update) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var inlineKeyboardButton1 = new InlineKeyboardButton();
        var inlineKeyboardButton2 = new InlineKeyboardButton();
        var inlineKeyboardButton3 = new InlineKeyboardButton();

        inlineKeyboardButton1.setText(messageMakerService.getTextFromProperties(
                update, "whoWillSing.b1.m"));
        inlineKeyboardButton1.setCallbackData(VOICE_ADD_COMMAND.toString());

        inlineKeyboardButton2.setText(messageMakerService.getTextFromProperties(
                update, "whoWillSing.b2.m"));
        inlineKeyboardButton2.setCallbackData(VOICE_ADD_COMMAND.toString());

        inlineKeyboardButton3.setText(messageMakerService.getTextFromProperties(
                update, "whoWillSing.b3.m"));
        inlineKeyboardButton3.setCallbackData(VOICE_ADD_COMMAND.toString());

        rowInline.add(inlineKeyboardButton1);
        rowInline.add(inlineKeyboardButton2);
        rowInline.add(inlineKeyboardButton3);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", userState));
        }
        repoController.setUserState(update, userState);
    }
}
