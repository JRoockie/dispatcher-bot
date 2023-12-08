package org.voetsky.dispatcherBot.services.logic.commands.command;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.repo.RepoController;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_BUTTON;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.*;

@Log4j
@AllArgsConstructor
public class ChoosingNameOrAnotherWayCommand implements Command {

    private final String action = CHOOSING_NAME_OR_ANOTHER_WAY.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        String username = repoController.getClientName(update);
        String text = String.format(
                messageMakerService.getTextFromProperties(
                        update,"choosingNameOrAnotherWay.h.m") , username);
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(update);
        var msg = messageMakerService.makeSendMessage(update, text, markupInline);
        changeState(update, AWAITING_FOR_BUTTON);
        return msg;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(Update update) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var inlineKeyboardButton1 = new InlineKeyboardButton();
        var inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText(
                messageMakerService.getTextFromProperties(update, "yes.m"));
        inlineKeyboardButton1.setCallbackData(SONG_ADD_AND_ADD_SONG_NAME_COMMAND.toString());
        inlineKeyboardButton2.setText(
                messageMakerService.getTextFromProperties(update, "no.m"));
        inlineKeyboardButton2.setCallbackData(START_COMMAND.toString());
        rowInline.add(inlineKeyboardButton1);
        rowInline.add(inlineKeyboardButton2);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    @Override
    public SendMessage callback(Update update) {
        if (update.getMessage().getText().equals(
                SONG_ADD_AND_ADD_SONG_NAME_COMMAND.toString())) {
            return messageMakerService.makeSendMessage(update, SONG_ADD_AND_ADD_SONG_NAME_COMMAND.toString());
        } else if (update.getMessage().getText().equals(
                START_COMMAND.toString())) {
            return messageMakerService.makeSendMessage(update, START_COMMAND.toString());
        }
        return messageMakerService.makeSendMessage(update, String.format("Ошибка в %s", ChoosingNameOrAnotherWayCommand.class));
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", AWAITING_FOR_BUTTON));
        }
        repoController.setUserState(update, userState);
    }
}
