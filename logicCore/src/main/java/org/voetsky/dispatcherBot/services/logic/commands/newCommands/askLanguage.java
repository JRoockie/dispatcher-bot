//package org.voetsky.dispatcherBot.services.logic.commands.newCommands;
//
//import lombok.AllArgsConstructor;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.voetsky.dispatcherBot.LocalizationInit.Localization.Localization;
//import org.voetsky.dispatcherBot.UserState;
//import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
//
//@AllArgsConstructor
//public class askLanguage implements Command {
//
//    private final Localization i18n;
//
//    @Override
//    public SendMessage handle(Update update) {
//        var msg = update.getMessage();
//        var chatId = msg.getChatId().toString();
//        var out = new StringBuilder();
//        var lang = sessionTg.get(chatId, "lang", "en");
//        out.append(i18n.get(lang, "menu.title")).append("\n");
//        out.append(i18n.get(lang, "command.lang")).append("\n");
//        return Optional.of(new SendMessage(chatId, out.toString()));
//    }
//
//    @Override
//    public SendMessage callback(Update update) {
//        return null;
//    }
//
//    @Override
//    public void changeState(Update update, UserState userState) {
//
//    }
//}
