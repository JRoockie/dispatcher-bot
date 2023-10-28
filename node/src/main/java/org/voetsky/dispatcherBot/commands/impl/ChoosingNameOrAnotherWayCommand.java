package org.voetsky.dispatcherBot.commands.impl;

import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.CommandInterface;
import org.voetsky.dispatcherBot.controller.CommandHandler;

import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.*;
import static org.voetsky.dispatcherBot.commands.Commands.SONG_ADD_AND_ADD_SONG_NAME_COMMAND;
import static org.voetsky.dispatcherBot.commands.Commands.START_COMMAND;

@Log4j
public class ChoosingNameOrAnotherWayCommand implements CommandInterface {

    private final String action;
    private final CommandHandler commandHandler;

    public ChoosingNameOrAnotherWayCommand(String action, CommandHandler commandHandler) {
        this.action = action;
        this.commandHandler = commandHandler;
    }

    @Override
    public SendMessage handle(Update update) {
        String username =commandHandler.getClientName(update);
        String text = "Чтобы мы могли максимально точно подобрать для вас фонограмму, вам необходимо ответить на следующие вопросы: " +
                "\n\n🙋"+username+", Вы знаете исполнителя и название песни?";
        changeState(update, AWAITING_FOR_BUTTON);
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

        SendMessage sendMessage = new SendMessage(commandHandler.findTelegramUserIdFromUpdate(update).getId().toString(), text);
        sendMessage.setReplyMarkup(markupInline);
        sendMessage.setText(text);
        return commandHandler.send(sendMessage);
    }

    @Override
    public SendMessage callback(Update update) {
        if (update.getMessage().getText().equals(SONG_ADD_AND_ADD_SONG_NAME_COMMAND.toString())){
            return commandHandler.updateReceiver(update);
        } else if (update.getMessage().getText().equals(START_COMMAND.toString())){
            return commandHandler.updateReceiver(update);
        }
        return commandHandler.send(update, "ошибка в " + ChoosingNameOrAnotherWayCommand.class);
    }

    @Override
    public void changeState(Update update, UserState userState) {
        log.debug("State changed to " + userState.toString());
        commandHandler.setUserState(update,userState);
    }
}
