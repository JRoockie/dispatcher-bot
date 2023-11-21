package org.voetsky.dispatcherBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entity.TgUser;

public interface TgUserRepository extends JpaRepository<TgUser,Long> {

    TgUser findByTelegramUserId(Long telegramUserId);

}
