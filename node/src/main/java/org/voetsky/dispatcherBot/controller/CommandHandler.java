package org.voetsky.dispatcherBot.controller;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.CommandInterface;
import org.voetsky.dispatcherBot.commands.impl.*;
import org.voetsky.dispatcherBot.entity.TgUser;
import org.voetsky.dispatcherBot.services.MessageService;
import org.voetsky.dispatcherBot.services.impl.BigDaoService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j
@Controller
@Getter
public class CommandHandler {

    private final Map<String, String> bindingBy = new ConcurrentHashMap<>();
    private final MessageService messageService;
    private Map<String, CommandInterface> actions;

//    private final OrderClientDao orderClientDao;
//    private final SongDao songDao;
//    private final TgUserDao tgUserDao;
    private final BigDaoService bigDaoService;

    public CommandHandler(MessageService messageService, BigDaoService bigDaoService) {
        this.messageService = messageService;
        this.bigDaoService = bigDaoService;
//        this.orderClientDao = orderClientDao;
//        this.songDao = songDao;
//        this.tgUserDao = tgUserDao;
        init();
    }

    @PostConstruct
    public void init() {
        actions = Map.of(
                "/start", new StartCommand(
                        List.of(
                                "/start - Команды бота",
                                "/echo - Ввод данных для команды",
                                "/songNameCommand"), this),
                "/askNameCommand", new AskNameCommand("/askNameCommand", this),
                "/songNameCommand", new SongNameCommand("/songNameCommand", this),
                "/songAddAndSongNameCommand", new SongAddAndSongNameCommand("/songAddAndSongNameCommand", this),
                "/choosingNameOrAnotherWay", new ChoosingNameOrAnotherWayCommand("/choosingNameOrAnotherWay", this));



    }

    public SendMessage updateReceiver(Update update) {

        log.debug("CONTROLLER: Choosing command");
        if (update.hasMessage()) {
            var key = update.getMessage().getText();
            var chatId = update.getMessage().getChatId().toString();
            if (actions.containsKey(key)) {
                log.debug("CONTROLLER: Executing NON-CALLBACK command part: " + actions.get(key));
                var msg = actions.get(key).handle(update);
                bindingBy.put(chatId, key);
                return msg;
            } else if (bindingBy.containsKey(chatId)) {
                var msg = actions.get(bindingBy.get(chatId)).callback(update);
                log.debug("CONTROLLER: Executing CALLBACK command part : " +
                        bindingBy.get(chatId));
//                bindingBy.remove(chatId);
                return msg;
            }

        } else if (update.hasCallbackQuery()) {
            log.debug("CONTROLLER: Executing BUTTON ");
            return buttonExecute(update);
        }
        log.debug("Callback doesn't found, command not found");
        return send(update, "Command not found, callback not found.");
    }

    public SendMessage buttonExecute(Update update) {
        try {
            User user = update.getCallbackQuery().getFrom();
            String callBack = update.getCallbackQuery().getData();
            log.debug("CONTROLLER: Button has pressed by: " + user.getId() + " with attribute: " + callBack);
            if (actions.containsKey(callBack)) {
                SendMessage sendMessage = actions.get(callBack).handle(update);
                bindingBy.put(update.getCallbackQuery().getFrom().getId().toString(), callBack);
                return send(sendMessage);
            } else {
                throw new RuntimeException("CONTROLLER: Button command not found");
            }
        } catch (RuntimeException e) {
            return send(update, "Ошибка нажатия кнопки. Введите /start ");
        }
    }

    public TgUser findOrSaveAppUser(User telegramUser) {
        return bigDaoService.findOrSaveAppUser(telegramUser);
    }

    public TgUser findAppUsersByTelegramUserId(Long id) {
        return bigDaoService.findAppUsersByTelegramUserId(id);
    }

    public User findTelegramUserIdFromUpdate(Update update) {
        return bigDaoService.findTelegramUserIdFromUpdate(update);
    }

    public void setUserState(User user, UserState userState) {
        bigDaoService.setState(user,userState);
    }

    public String getClientName(Update update) {
        return bigDaoService.getClientName(update);
    }

    public SendMessage send(Update update, String text) {
        return messageService.send(update, text);
    }

    public SendMessage send(SendMessage sendMessage) {
        return messageService.send(sendMessage);
    }




}
