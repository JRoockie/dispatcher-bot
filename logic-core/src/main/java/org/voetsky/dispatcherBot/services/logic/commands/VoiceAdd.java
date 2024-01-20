package org.voetsky.dispatcherBot.services.logic.commands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.EditSong;
import org.voetsky.dispatcherBot.services.logic.commands.commandFunctions.InlineKeyboard;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMaker;
import org.voetsky.dispatcherBot.services.repoServices.mainRepoService.MainRepo;

import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_BUTTON;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.SHOW_PRICE;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.SONG_NAME_OR_MP3;

@Log4j
@AllArgsConstructor
public class VoiceAdd implements Command, EditSong, InlineKeyboard {
    private final MainRepo mainRepo;
    private final MessageMaker messageMaker;

    @Override
    public SendMessage handle(Update update) {
        String text = messageMaker.getTextFromProperties(
                update, "voiceAddCommand.h.m");
        return messageMaker.makeSendMessage(update, text, false);
    }

    @Override
    public SendMessage callback(Update update) {
        String text = messageMaker.getTextFromProperties(
                update, "voiceAddCommand.c.m");
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(update);
        var msg = messageMaker.makeSendMessage(update, text, markupInline, false);
        editSong(update);
        changeState(update, AWAITING_FOR_BUTTON);
        return msg;
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", AWAITING_FOR_BUTTON));
        }
        mainRepo.setUserState(update, userState);
    }

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup(Update update) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var inlineKeyboardButton1 = new InlineKeyboardButton();
        var inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText(
                messageMaker.getTextFromProperties(update, "yes.m"));
        inlineKeyboardButton1.setCallbackData(SONG_NAME_OR_MP3.toString());
        inlineKeyboardButton2.setText(
                messageMaker.getTextFromProperties(update, "no.m"));
        inlineKeyboardButton2.setCallbackData(SHOW_PRICE.toString());
        rowInline.add(inlineKeyboardButton1);
        rowInline.add(inlineKeyboardButton2);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    @Override
    public void editSong(Update update) {
        mainRepo.addVoice(update);
    }
}
