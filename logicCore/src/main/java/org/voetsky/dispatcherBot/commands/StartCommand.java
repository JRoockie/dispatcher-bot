package org.voetsky.dispatcherBot.commands;


import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.interf.CommandInterface;
import org.voetsky.dispatcherBot.controller.CommandHandler;

import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.*;
import static org.voetsky.dispatcherBot.commands.Commands.ASK_NAME_COMMAND;

@Log4j
public class StartCommand implements CommandInterface {

    private final String action;
    private final CommandHandler commandHandler;

    public StartCommand(String action, CommandHandler commandHandler) {
        this.action = action;
        this.commandHandler = commandHandler;
    }

    @Override
    public SendMessage handle(Update update) {
        String out = "Здравствуйте, вас привествует чат бот VocalPlus. Я помогу вам выбрать время для звукозаписи в нашей студии.\n" + "\n";

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(ASK_NAME_COMMAND.toString());
        inlineKeyboardButton.setText("Начать");
        rowInline.add(inlineKeyboardButton);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        SendMessage sendMessage = new SendMessage(commandHandler.findTelegramUserIdFromUpdate(update).getId().toString(), out);
        sendMessage.setReplyMarkup(markupInline);


// todo make new order

//        commandHandler.getBigDaoService().addNewOrder(update);

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
