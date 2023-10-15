package org.voetsky.dispatcherBot.services.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.controller.CommandController;
import org.voetsky.dispatcherBot.dao.AppUserDao;
import org.voetsky.dispatcherBot.dao.OrderDao;
import org.voetsky.dispatcherBot.dao.SongDao;
import org.voetsky.dispatcherBot.entity.AppUser;
import org.voetsky.dispatcherBot.services.MainService;

@Service
@Log4j
public class MainServiceImpl implements MainService {

    private final CommandController controller;
    private final ProducerServiceImpl producerService;
    private final OrderDao orderDao;
    private final SongDao songDao;
    private final AppUserDao appUserDao;

    public MainServiceImpl(CommandController controller, ProducerServiceImpl producerService, OrderDao orderDao, SongDao songDao, AppUserDao appUserDao) {
        this.controller = controller;
        this.producerService = producerService;
        this.orderDao = orderDao;
        this.songDao = songDao;
        this.appUserDao = appUserDao;
    }

    public AppUser findOrSaveAppUser(User telegramUser){
        AppUser persistentAppUser = appUserDao.findAppUsersByTelegramUserId(telegramUser.getId());
        if (persistentAppUser == null){
            //Объект еще не представлен в бд и его предстоит сохранить
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .build();
            return appUserDao.save(transientAppUser);
        }
        return persistentAppUser;
    }
    @Override
    public void processTextMessage(Update update) {
        var textMessage = update.getMessage();
        var telegramUser = textMessage.getFrom();
        var appUser = findOrSaveAppUser(telegramUser);



        log.debug("NODE: Text message is received");
        producerService.producerAnswer(controller.updateReceiver(update));
    }

    public void consumeDocMessageUpdates(Update update) {
        log.debug("NODE: Doc message is received");
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Doc message is received from NODE");
        producerService.producerAnswer(sendMessage);
    }

    public void consumeVoiceMessageUpdates(Update update) {
        log.debug("NODE: Voice message is received");
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Voice message is received from NODE");
        producerService.producerAnswer(sendMessage);
    }

    public void consumeButtonUpdates(Update update) {
        log.debug("NODE: Button message is received");
        var sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getId());
        sendMessage.setText("Button click is received from NODE");
        producerService.producerAnswer(controller.updateReceiver(update));
    }


}
