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

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_BUTTON;
import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_TEXT;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.*;

@Log4j
@AllArgsConstructor
public class SongName implements Command {

    private final String action = SONG_NAME.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        String text = messageMakerService.getTextFromProperties(
                update, "songName.h.m");
        changeState(update, AWAITING_FOR_TEXT);
        return messageMakerService.makeSendMessage(update, text);
    }

    @Override
    public SendMessage callback(Update update) {
        if (!update.hasMessage()) {
            return forceCallback(update);
        }
        String text = String.format(
                messageMakerService.getTextFromProperties(
                        update, "songName.c.m"),
                update.getMessage().getText());

        String songName = update.getMessage().getText();
        Song newSong = Song.builder()
                .songName(songName)
                .build();

        repoController.addSong(update, newSong);
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(update);
        var msg = messageMakerService.makeSendMessage(update, text, markupInline);
        changeState(update, AWAITING_FOR_BUTTON);
        return msg;
    }

    public SendMessage forceCallback(Update update) {
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(update);
        String text = messageMakerService.getTextFromProperties(
                update, "songName.c.m");
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
                messageMakerService.getTextFromProperties(update, "songName.b1.m"));
        inlineKeyboardButton1.setCallbackData(ADD_LINK.toString());
        inlineKeyboardButton2.setText(
                messageMakerService.getTextFromProperties(update, "songName.b2.m"));
        inlineKeyboardButton2.setCallbackData(MP3_ADD_COMMAND.toString());
        rowInline.add(inlineKeyboardButton1);
        rowInline.add(inlineKeyboardButton2);
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
