package org.voetsky.dispatcherBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entity.TgUser;

public interface TgUserDao extends JpaRepository<TgUser,Long> {

    TgUser findAppUsersByTelegramUserId(Long telegramUserId);

}
