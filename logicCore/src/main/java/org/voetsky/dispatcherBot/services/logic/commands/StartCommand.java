package org.voetsky.dispatcherBot.services.logic.commands;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.logic.commands.command.CommandInterface;
import org.voetsky.dispatcherBot.services.repo.RepoController;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;

import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_BUTTON;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.ASK_NAME_COMMAND;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.START_COMMAND;

@Log4j
@AllArgsConstructor
public class StartCommand implements CommandInterface {

    private final String action = START_COMMAND.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        String text = "Здравствуйте, вас привествует чат бот VocalPlus."
                + " Я помогу вам выбрать время для звукозаписи в нашей студии.\n" + "\n";
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup();
        changeState(update, AWAITING_FOR_BUTTON);
        return messageMakerService.makeSendMessage(update, text, markupInline);
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(ASK_NAME_COMMAND.toString());
        inlineKeyboardButton.setText("Начать");
        rowInline.add(inlineKeyboardButton);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    @Override
    public SendMessage callback(Update update) {
        return handle(update);
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", AWAITING_FOR_BUTTON));
        }
        repoController.setUserState(update, userState);
    }

}
