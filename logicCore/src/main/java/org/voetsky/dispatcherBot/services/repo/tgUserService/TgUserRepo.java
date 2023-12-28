package org.voetsky.dispatcherBot.services.repo.tgUserService;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;

public interface TgUserRepo {

    TgUser findOrSaveAppUser(User telegramUser);

    TgUser findAppUsersByTelegramUserId(Long id);

    User findUserIdFromUpdate(Update update);

    void setState(Update update, UserState userState);

    TgUser save(TgUser tgUser);

    UserState getState(Update update);

    TgUser getTgUserFromUpdate(Update update);

    Long getIdFromUpdate(Update update);

    void setCurrentSong(Update update, Long songId);

    void addOrderToTgUser(TgUser tgUser, OrderClient orderClient);
}
