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
import org.voetsky.dispatcherBot.services.repoServices.comparingEntityService.ComparingEntityServiceImpl;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_COMMAND;

@Log4j
@AllArgsConstructor
@Service
public class TgUserRepositoryService implements TgUserRepo {

    private final TgUserRepository tgUserRepository;
    private final ComparingEntityServiceImpl comparingEntityService;

    public TgUser findOrSaveAppUser(User telegramUser) {
        //todo раскомментить после тестов
        TgUser persistentTgUser = tgUserRepository.findByTelegramUserId(telegramUser.getId());
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

    public String getLocalization(Update update){
        TgUser tgUser = tgUserRepository
                .findByTelegramUserId(findUserIdFromUpdate(update).getId());
        return tgUser.getLocalization();
    }

    public void setLocalization(Update update, String language){
        TgUser tgUser = tgUserRepository
                .findByTelegramUserId(findUserIdFromUpdate(update).getId());
        tgUser.setLocalization(language);
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
