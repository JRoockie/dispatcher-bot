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

@Log4j
public class StartCommand implements CommandInterface {

    private final List<String> actions;
    private final CommandHandler commandHandler;

    public StartCommand(List<String> actions, CommandHandler controller) {
        this.actions = actions;
        this.commandHandler = controller;
    }

    @Override
    public SendMessage handle(Update update) {
        String out = "Здравствуйте, вас привествует чат бот VocalPlus. Я помогу вам выбрать время для звукозаписи в нашей студии.\n" + "\n";

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData("/askNameCommand");
        inlineKeyboardButton.setText("Начать");
        rowInline.add(inlineKeyboardButton);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        SendMessage sendMessage = new SendMessage(commandHandler.findTelegramUserIdFromUpdate(update).getId().toString(), out);
        sendMessage.setReplyMarkup(markupInline);

        changeState(update, AWAITING_FOR_BUTTON);
        return commandHandler.send(sendMessage);

    }

    @Override
    public SendMessage callback(Update update) {
        return handle(update);
    }

    @Override
    public void changeState(Update update, UserState userState) {
        log.debug("State changed to " + AWAITING_FOR_BUTTON);
        commandHandler.setUserState(update, userState);
    }


}
