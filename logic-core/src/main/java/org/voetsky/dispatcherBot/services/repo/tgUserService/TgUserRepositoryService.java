package org.voetsky.dispatcherBot.services.repo.tgUserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;
import org.voetsky.dispatcherBot.repository.tgUser.TgUserRepository;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_COMMAND;

@Log4j
@AllArgsConstructor
@Service
public class TgUserRepositoryService implements TgUserRepo {

    private final TgUserRepository tgUserRepositoryJpa;

    public TgUser findOrSaveAppUser(User telegramUser) {
        TgUser persistentTgUser = tgUserRepositoryJpa.findByTelegramUserId(telegramUser.getId());
        if (persistentTgUser == null) {
            if (log.isDebugEnabled()) {
                log.debug("NEW user in system, adding: " + telegramUser.getId());
            }
            return makeTgUser(telegramUser);
        }
        if (log.isDebugEnabled()) {
            log.debug("User ALREADY in system");
        }
        return persistentTgUser;
    }

    private TgUser makeTgUser(User telegramUser) {
        TgUser transientTgUser = TgUser.builder()
                .telegramUserId(telegramUser.getId())
                .username(telegramUser.getUserName())
                .firstName(telegramUser.getFirstName())
                .lastName(telegramUser.getLastName())
                .userState(AWAITING_FOR_COMMAND)
                .localization("ru")
                .build();

        transientTgUser = tgUserRepositoryJpa.save(transientTgUser);
        return transientTgUser;
    }

    @Override
    public TgUser getTgUserFromUpdate(Update update) {
        return
                findAppUsersByTelegramUserId(
                        getIdFromUpdate(update));
    }

    public Long getIdFromUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        } else {
            return update.getMessage().getChatId();
        }
    }

    public TgUser findAppUsersByTelegramUserId(Long id) {
        return tgUserRepositoryJpa.findByTelegramUserId(id);
    }

    @Override
    public void addOrderToTgUser(TgUser tgUser, OrderClient orderClient) {
        tgUser.setCurrentOrderId(orderClient.getId());
        tgUserRepositoryJpa.save(tgUser);
    }

    public User findUserIdFromUpdate(Update update) {
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
        TgUser tgUser = tgUserRepositoryJpa.findByTelegramUserId(findUserIdFromUpdate(update).getId());
        tgUser.setUserState(userState);
        tgUserRepositoryJpa.save(tgUser);
    }

    public void setCurrentSong(Update update, Long songId) {
        TgUser tgUser = tgUserRepositoryJpa.findByTelegramUserId(findUserIdFromUpdate(update).getId());
        tgUser.setCurrentSongId(songId);
        tgUserRepositoryJpa.save(tgUser);
    }

    public TgUser save(TgUser tgUser) {
        return tgUserRepositoryJpa.save(tgUser);
    }

    public UserState getState(Update update) {

        UserState state;
        TgUser tgUser;

        if (update.hasCallbackQuery()) {
            tgUser = findOrSaveAppUser(update.getCallbackQuery().getFrom());
        } else {
            tgUser = findOrSaveAppUser(update.getMessage().getFrom());
        }
        state = tgUser.getUserState();
        return state;
    }

}
