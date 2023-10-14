package org.voetsky.dispatcherBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entity.AppUser;

public interface AppUserDao extends JpaRepository<AppUser,Long> {

    AppUser findAppUsersByTelegramUserId(Long id);

}
