package org.voetsky.dispatcherBot.commands.impl;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voetsky.dispatcherBot.commands.CommandInterface;
import org.voetsky.dispatcherBot.controller.CommandController;

import java.util.ArrayList;
import java.util.List;

public class StartCommand implements CommandInterface {

    private final List<String> actions;
    private final CommandController controller;

    public StartCommand(List<String> actions, CommandController controller) {
        this.actions = actions;
        this.controller = controller;
    }


    @Override
    public SendMessage handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var out = new StringBuilder();
        out.append("Здравствуйте, вас привествует чат бот VocalPlus. Я помогу вам выбрать время для звукозаписи в нашей студии.\n").append("\n");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData("/songNameCommand");
        inlineKeyboardButton.setText("Начать");
        rowInline.add(inlineKeyboardButton);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        SendMessage sendMessage = new SendMessage(chatId, out.toString());
        sendMessage.setReplyMarkup(markupInline);
        return sendMessage;

    }

    @Override
    public SendMessage callback(Update update) {
        return handle(update);
    }


}
