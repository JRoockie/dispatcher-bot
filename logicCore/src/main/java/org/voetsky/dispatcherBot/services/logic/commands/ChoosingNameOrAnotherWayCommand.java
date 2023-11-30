package org.voetsky.dispatcherBot.services.logic.commands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.logic.commands.command.CommandInterface;
import org.voetsky.dispatcherBot.services.repoAcess.RepoController;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;

import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_BUTTON;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.*;

@Log4j
@AllArgsConstructor
public class ChoosingNameOrAnotherWayCommand implements CommandInterface {

    private final String action = CHOOSING_NAME_OR_ANOTHER_WAY.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        String username = repoController.getClientName(update);
        String text = String.format("Чтобы мы могли максимально точно подобрать для вас"
                + " фонограмму, вам необходимо ответить на следующие вопросы:"
                + " \n\n🙋%s, Вы знаете исполнителя и название песни?", username);

        changeState(update, AWAITING_FOR_BUTTON);
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup();
        return messageMakerService.makeSendMessage(update, text, markupInline);
    }

    private static InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var inlineKeyboardButton1 = new InlineKeyboardButton();
        var inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Да");
        inlineKeyboardButton1.setCallbackData(SONG_ADD_AND_ADD_SONG_NAME_COMMAND.toString());
        inlineKeyboardButton2.setText("Нет");
        inlineKeyboardButton2.setCallbackData(START_COMMAND.toString());
        rowInline.add(inlineKeyboardButton1);
        rowInline.add(inlineKeyboardButton2);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    @Override
    public SendMessage callback(Update update) {
        if (update.getMessage().getText().equals(SONG_ADD_AND_ADD_SONG_NAME_COMMAND.toString())) {
            return messageMakerService.makeSendMessage(
                    update, SONG_ADD_AND_ADD_SONG_NAME_COMMAND.toString());
        } else if (update.getMessage().getText().equals(START_COMMAND.toString())) {
            return messageMakerService.makeSendMessage(
                    update, START_COMMAND.toString());
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
