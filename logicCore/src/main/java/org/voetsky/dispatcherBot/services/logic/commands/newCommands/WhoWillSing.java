package org.voetsky.dispatcherBot.services.logic.commands.newCommands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.services.repo.RepoController;

import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.*;
import static org.voetsky.dispatcherBot.WhoWillSing.*;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.VOICE_ADD_COMMAND;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.WHO_WILL_SING;

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
        /// TODO: 14.12.2023 код не выполняется вообще, вынести эту часть в отдельную команду
        String callbackMessage = update.getCallbackQuery().getData();
        Song song = new Song();

        if (callbackMessage.equals(messageMakerService
                .getTextFromProperties(update, "whoWillSing.b1.m"))) {

            song.setWhoWillSing(ADULT);
            repoController.updateSong(update, song);

        } else if (callbackMessage.equals(messageMakerService
                .getTextFromProperties(update, "whoWillSing.b2.m"))) {

            song.setWhoWillSing(ADULT_AND_CHILD);
            repoController.updateSong(update, song);

        } else if (callbackMessage.equals(messageMakerService
                .getTextFromProperties(update, "whoWillSing.b3.m"))) {
            song.setWhoWillSing(CHILD);
            repoController.updateSong(update, song);
        }

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

        String b1 = messageMakerService.getTextFromProperties(
                update, "whoWillSing.b1.m");
        String b2 = messageMakerService.getTextFromProperties(
                update, "whoWillSing.b2.m");
        String b3 = messageMakerService.getTextFromProperties(
                update, "whoWillSing.b3.m");

        inlineKeyboardButton1.setText(b1);
        inlineKeyboardButton1.setCallbackData(b1);
        inlineKeyboardButton2.setText(b2);
        inlineKeyboardButton2.setCallbackData(b2);
        inlineKeyboardButton3.setText(b3);
        inlineKeyboardButton3.setCallbackData(b3);

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
