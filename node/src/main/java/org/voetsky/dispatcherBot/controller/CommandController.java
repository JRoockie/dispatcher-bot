package org.voetsky.dispatcherBot.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.commands.CommandInterface;
import org.voetsky.dispatcherBot.commands.impl.SongAddCommand;
import org.voetsky.dispatcherBot.commands.impl.SongNameCommand;
import org.voetsky.dispatcherBot.commands.impl.StartCommand;
import org.voetsky.dispatcherBot.dao.AppUserDao;
import org.voetsky.dispatcherBot.dao.imp.OrderDaoImpl;
import org.voetsky.dispatcherBot.services.MessageService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j
@Controller
public class CommandController {

    private final OrderDaoImpl orderDaoImpl;
    private final AppUserDao appUserDao;
    private final Map<String, String> bindingBy = new ConcurrentHashMap<>();
    private final MessageService messageService;
    private Map<String, CommandInterface> actions;

    public CommandController(OrderDaoImpl orderDaoImpl, AppUserDao appUserDao, MessageService messageService) {
        this.orderDaoImpl = orderDaoImpl;
        this.appUserDao = appUserDao;
        this.messageService = messageService;
        init();
    }

    public void init() {

        actions = Map.of(
                "/start", new StartCommand(
                        List.of(
                                "/start - Команды бота",
                                "/echo - Ввод данных для команды",
                                "/songNameCommand"), this),
                "/songNameCommand", new SongNameCommand("/songNameCommand", this),
                "/songAddCommand", new SongAddCommand("/songAddCommand", this));
    }

    public SendMessage send(Update update, String text) {
        return messageService.send(update, text);
    }


    public SendMessage updateReceiver(Update update) {

        if (update.hasMessage()) {

            var key = update.getMessage().getText();
            var chatId = update.getMessage().getChatId().toString();
            if (actions.containsKey(key)) {
                var msg = actions.get(key).handle(update);
                bindingBy.put(chatId, key);
                return msg;
            } else if (bindingBy.containsKey(chatId)) {
                var msg = actions.get(bindingBy.get(chatId)).callback(update);
                bindingBy.remove(chatId);
                return msg;
            }

        } else if (update.hasCallbackQuery()) {
            return callbackExecute(update);
        }
        return null;
    }

    public SendMessage chainOfCommands(Update update) {

        //добавить песню и заполнить ее
        return null;
    }


    public SendMessage callbackExecute(Update update) {
        try {
            String callBack = update.getCallbackQuery().getData();
            log.debug("Button attribute: " + callBack);
            if (actions.containsKey(callBack)) {
                SendMessage sendMessage = actions.get(callBack).handle(update);
                bindingBy.put(update.getCallbackQuery().getFrom().getId().toString(), callBack);
                return sendMessage;
            }
        } catch (RuntimeException e) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getCallbackQuery().getFrom().getId());
            sendMessage.setText("Ошибка нажатия кнопки. Введите /start");
            return sendMessage;
        }
        return null;
    }
}
