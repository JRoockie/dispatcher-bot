package org.voetsky.dispatcherBot.controller;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.commands.CommandInterface;
import org.voetsky.dispatcherBot.commands.impl.SongAddCommand;
import org.voetsky.dispatcherBot.commands.impl.SongNameCommand;
import org.voetsky.dispatcherBot.commands.impl.StartCommand;
import org.voetsky.dispatcherBot.dao.OrderClientDao;
import org.voetsky.dispatcherBot.dao.SongDao;
import org.voetsky.dispatcherBot.dao.TgUserDao;
import org.voetsky.dispatcherBot.entity.TgUser;
import org.voetsky.dispatcherBot.services.MessageService;
import org.voetsky.dispatcherBot.services.impl.MainServiceImpl;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_COMMAND;

@Log4j
@Controller
@Getter
public class CommandHandler {

    private final Map<String, String> bindingBy = new ConcurrentHashMap<>();
    private final MessageService messageService;
    private Map<String, CommandInterface> actions;

    private final OrderClientDao orderClientDao;
    private final SongDao songDao;
    private final TgUserDao tgUserDao;

    public CommandHandler( MessageService messageService,
                          OrderClientDao orderClientDao,
                          SongDao songDao,
                          TgUserDao tgUserDao) {

        this.messageService = messageService;
        this.orderClientDao = orderClientDao;
        this.songDao = songDao;
        this.tgUserDao = tgUserDao;
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
                "/songNameCommand", new SongNameCommand("/songNameCommand", this),
                "/songAddCommand", new SongAddCommand("/songAddCommand", this));
    }

    public SendMessage send(Update update, String text) {
        return messageService.send(update, text);
    }

    public SendMessage send(SendMessage sendMessage) {
        return messageService.send(sendMessage);
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
                bindingBy.remove(chatId);
                return msg;
            }

        } else if (update.hasCallbackQuery()) {
            return buttonExecute(update);
        }
        log.debug("Callback doesn't found, command not found");
        return messageService.send(update, "Command not found, callback not found.");
    }

    public SendMessage buttonExecute(Update update) {
        try {
            User user = update.getCallbackQuery().getFrom();
            String callBack = update.getCallbackQuery().getData();
            log.debug("CONTROLLER: Button has pressed by: " + user.getId() + " with attribute: " + callBack);
            if (actions.containsKey(callBack)) {
                SendMessage sendMessage = actions.get(callBack).handle(update);
                bindingBy.put(update.getCallbackQuery().getFrom().getId().toString(), callBack);
                return sendMessage;
            }
        } catch (RuntimeException e) {
            return send(update,"Ошибка нажатия кнопки. Введите /start ");
        }
        return null;
    }

    public TgUser findOrSaveAppUser(org.telegram.telegrambots.meta.api.objects.User telegramUser) {
        TgUser persistentTgUser = tgUserDao.findAppUsersByTelegramUserId(telegramUser.getId());
        if (persistentTgUser == null) {
            log.debug("NODE: NEW user in system, adding: " + telegramUser.getId());
            //Объект еще не представлен в бд и его предстоит сохранить
            TgUser transientTgUser = TgUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .userState(AWAITING_FOR_COMMAND)
                    .build();
            return tgUserDao.save(transientTgUser);
        }
        log.debug("NODE: User ALREADY in system");
        return persistentTgUser;
    }

    public TgUser findAppUsersByTelegramUserId(Long id){
        return tgUserDao.findAppUsersByTelegramUserId(id);
    }


}
