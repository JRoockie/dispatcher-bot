package org.voetsky.dispatcherBot.services.logic.commands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.Chain;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.EditSong;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.InlineKeyboard;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMaker;
import org.voetsky.dispatcherBot.services.repoServices.mainRepoService.MainService;

import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_BUTTON;
import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_VOICE;
import static org.voetsky.dispatcherBot.WhoWillSing.*;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.VOICE_ADD;

@Log4j
@AllArgsConstructor
public class WhoWillSing implements Command, Chain, EditSong, InlineKeyboard {
    private final MainService mainRepoService;
    private final MessageMaker messageMaker;

    @Override
    public SendMessage handle(Update update) {
        String text = messageMaker.getTextFromProperties(
                update, "whoWillSing.h.m");
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(update);
        var msg = messageMaker.makeSendMessage(update, text, markupInline);
        changeState(update, AWAITING_FOR_BUTTON);
        return msg;
    }

    @Override
    public SendMessage callback(Update update) {
        editSong(update);
        var msg = putNextCommand(update, VOICE_ADD.toString());
        changeState(update, AWAITING_FOR_VOICE);
        return msg;
    }

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup(Update update) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var inlineKeyboardButton1 = new InlineKeyboardButton();
        var inlineKeyboardButton2 = new InlineKeyboardButton();
        var inlineKeyboardButton3 = new InlineKeyboardButton();

        String b1 = messageMaker.getTextFromProperties(
                update, "whoWillSing.b1.m");
        String b2 = messageMaker.getTextFromProperties(
                update, "whoWillSing.b2.m");
        String b3 = messageMaker.getTextFromProperties(
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
        mainRepoService.setUserState(update, userState);
    }

    @Override
    public SendMessage putNextCommand(Update update, String command) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Put command for chain evoke %S", command));
        }
        return messageMaker.makeSendMessage(update, command);
    }

    @Override
    public void editSong(Update update) {
        String callbackMessage = update.getCallbackQuery().getData();
        Song song = createDefaultSong();

        if (callbackMessage.equals(messageMaker
                .getTextFromProperties(update, "whoWillSing.b2.m"))) {
            song.setWhoWillSing(ADULT_AND_CHILD);

        } else if (callbackMessage.equals(messageMaker
                .getTextFromProperties(update, "whoWillSing.b3.m"))) {
            song.setWhoWillSing(CHILD);
        }
        mainRepoService.updateSong(update, song);
        mainRepoService.fillSongNullFields(update);
    }

    private Song createDefaultSong() {
        Song song = new Song();
        song.setWhoWillSing(ADULT);
        song.setFilled(true);
        return song;
    }
}
