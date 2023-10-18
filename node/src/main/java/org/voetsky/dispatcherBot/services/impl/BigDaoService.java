package org.voetsky.dispatcherBot.services.impl;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.dao.OrderClientDao;
import org.voetsky.dispatcherBot.dao.SongDao;
import org.voetsky.dispatcherBot.dao.TgUserDao;
import org.voetsky.dispatcherBot.entity.TgUser;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_COMMAND;

@Component
@Getter
@Log4j
public class BigDaoService {
    private final OrderClientDao orderClientDao;
    private final SongDao songDao;
    private final TgUserDao tgUserDao;

    public BigDaoService(OrderClientDao orderClientDao, SongDao songDao, TgUserDao tgUserDao) {
        this.orderClientDao = orderClientDao;
        this.songDao = songDao;
        this.tgUserDao = tgUserDao;
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

    public TgUser findAppUsersByTelegramUserId(Long id) {
        return tgUserDao.findAppUsersByTelegramUserId(id);
    }

    public User findTelegramUserIdFromUpdate(Update update) {
        User user;
        if (update.getMessage() != null) {
            user = update.getMessage().getFrom();
            return user;
        } else {
            user = update.getCallbackQuery().getFrom();
            return user;
        }
    }

    public void setState(Update update, UserState userState) {
        TgUser tgUser = tgUserDao.findAppUsersByTelegramUserId(findTelegramUserIdFromUpdate(update).getId());
        tgUser.setUserState(userState);
        tgUserDao.save(tgUser);
    }

    public String getClientName(Update update) {
        TgUser tgUser = tgUserDao.findAppUsersByTelegramUserId(findTelegramUserIdFromUpdate(update).getId());
        return tgUser.getNameAsClient();
    }

    public void setClientName(Update update, String clientName) {
        TgUser tgUser = tgUserDao.findAppUsersByTelegramUserId(findTelegramUserIdFromUpdate(update).getId());
        tgUser.setNameAsClient(clientName);
        tgUserDao.save(tgUser);
    }

}
