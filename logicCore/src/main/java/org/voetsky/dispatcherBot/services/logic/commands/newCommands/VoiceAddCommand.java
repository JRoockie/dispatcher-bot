package org.voetsky.dispatcherBot.services.logic.commands.newCommands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.repo.RepoController;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;

import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.*;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.*;

@Log4j
@AllArgsConstructor
public class VoiceAddCommand implements Command {

    private final String action = VOICE_ADD_COMMAND.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        //if it will be child - ask everytime, if adult - only one
        String s = update.getMessage().getText();
        String text = messageMakerService.getTextFromProperties(
                update, "voiceAddCommand.h.m");
//        changeState(update, AWAITING_FOR_VOICE);
        return messageMakerService.makeSendMessage(update, text);
    }

    @Override
    public SendMessage callback(Update update) {
        String text = messageMakerService.getTextFromProperties(
                update, "voiceAddCommand.c.m");
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(update);
        var msg = messageMakerService.makeSendMessage(update, text, markupInline);
        repoController.addVoice(update);
        changeState(update, AWAITING_FOR_BUTTON);
        return msg;
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", AWAITING_FOR_BUTTON));
        }
        repoController.setUserState(update, userState);
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(Update update) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var inlineKeyboardButton1 = new InlineKeyboardButton();
        var inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText(
                messageMakerService.getTextFromProperties(update, "yes.m"));
        inlineKeyboardButton1.setCallbackData(SONG_NAME_OR_MP3.toString());
        inlineKeyboardButton2.setText(
                messageMakerService.getTextFromProperties(update, "no.m"));
        inlineKeyboardButton2.setCallbackData(SHOW_PRICE.toString());
        rowInline.add(inlineKeyboardButton1);
        rowInline.add(inlineKeyboardButton2);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
