package org.voetsky.dispatcherBot.services.repoService.tgUserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.entity.OrderClient;
import org.voetsky.dispatcherBot.entity.TgUser;
import org.voetsky.dispatcherBot.repository.TgUserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_COMMAND;

@Component
@AllArgsConstructor
@Log4j
public class TgUserRepositoryService implements TgUserRepo {

    private final TgUserRepository tgUserRepository;

    public TgUser findOrSaveAppUser(User telegramUser) {
        //todo раскомментить после тестов
        TgUser persistentTgUser = tgUserRepository.findByTelegramUserId(telegramUser.getId());
//        TgUser persistentTgUser = null;
        if (persistentTgUser == null) {
            log.debug("NODE: NEW user in system, adding: " + telegramUser.getId());
            //Объект еще не представлен в бд и его предстоит сохранить
            return makeTgUser(telegramUser);
        }
        log.debug("NODE: User ALREADY in system");
        return persistentTgUser;
    }

    private TgUser makeTgUser(User telegramUser) {
        TgUser transientTgUser = TgUser.builder()
                .telegramUserId(telegramUser.getId())
                .username(telegramUser.getUserName())
                .firstName(telegramUser.getFirstName())
                .lastName(telegramUser.getLastName())
                .userState(AWAITING_FOR_COMMAND)
                .build();

        transientTgUser = tgUserRepository.save(transientTgUser);
        //orderClientRepositoryService.addOrder(transientTgUser);
        return transientTgUser;
    }

    public Long getIdFromUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        } else {
            return update.getMessage().getChatId();
        }
    }

    public TgUser findAppUsersByTelegramUserId(Long id) {
        return tgUserRepository.findByTelegramUserId(id);
    }

    public TgUser findByTelegramUserId(Long id) {
        return tgUserRepository.findByTelegramUserId(id);
    }

    public TgUser findTgUserIdFromUpdate(Update update) {
        return tgUserRepository.findByTelegramUserId(getIdFromUpdate(update));

    }

    @Override
    public void addOrderToTgUser(TgUser tgUser, OrderClient orderClient) {
        tgUser.setCurrentOrderId(orderClient.getId());
        tgUserRepository.save(tgUser);
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
        TgUser tgUser = tgUserRepository.findByTelegramUserId(findUserIdFromUpdate(update).getId());
        tgUser.setUserState(userState);
        tgUserRepository.save(tgUser);
    }

    public String getClientName(Update update) {
        TgUser tgUser = tgUserRepository.findByTelegramUserId(findUserIdFromUpdate(update).getId());
        return tgUser.getNameAsClient();
    }

    public void setClientName(Update update, String clientName) {
        TgUser tgUser = tgUserRepository.findByTelegramUserId(findUserIdFromUpdate(update).getId());
        tgUser.setNameAsClient(clientName);
        tgUserRepository.save(tgUser);
    }

    public void setCurrentSong(Update update, Long songId) {
        TgUser tgUser = tgUserRepository.findByTelegramUserId(findUserIdFromUpdate(update).getId());
        tgUser.setCurrentSongId(songId);
        tgUserRepository.save(tgUser);
    }

    public TgUser save(TgUser tgUser) {
        return tgUserRepository.save(tgUser);
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
