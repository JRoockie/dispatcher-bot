package org.voetsky.dispatcherBot.services.repoService.tgUserService;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.entity.TgUser;

public interface TgUserRepo {

    TgUser findOrSaveAppUser(User telegramUser);

    TgUser findAppUsersByTelegramUserId(Long id);

    TgUser findByTelegramUserId(Long id);

    User findUserIdFromUpdate(Update update);

    void setState(Update update, UserState userState);

    String getClientName(Update update);

    void setClientName(Update update, String clientName);

    TgUser save(TgUser tgUser);

    UserState getState(Update update);

    Long getIdFromUpdate(Update update);

    TgUser findTgUserIdFromUpdate(Update update);

}
