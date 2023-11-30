package org.voetsky.dispatcherBot.repository.tgUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TgUserRepository extends JpaRepository<TgUser, Long> {

    TgUser findByTelegramUserId(Long telegramUserId);

}
