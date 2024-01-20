package org.voetsky.dispatcherBot.services.logic.commands.commandFunctions;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface InlineKeyboard {

    InlineKeyboardMarkup getInlineKeyboardMarkup(Update update);

}
