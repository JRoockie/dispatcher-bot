package org.voetsky.dispatcherBot.services.impl;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.entity.*;
import org.voetsky.dispatcherBot.repository.*;
import org.voetsky.dispatcherBot.services.FileService;

import java.util.List;
import java.util.stream.Collectors;

import static org.voetsky.dispatcherBot.UserState.AWAITING_FOR_COMMAND;

@Component
@Getter
@Log4j
public class BigDaoService {
    private final OrderClientRepository orderClientRepository;
    private final SongRepository songRepository;
    private final TgUserRepository tgUserRepository;
    private final ComparingService comparingService;
    private final FileService fileService;
    private final TgAudioRepository tgAudioRepository;
    private final TgVoiceRepository tgVoiceRepository;

    public BigDaoService(OrderClientRepository orderClientRepository, SongRepository songRepository, TgUserRepository tgUserRepository, ComparingService comparingService, FileService fileService, TgAudioRepository tgAudioRepository, TgVoiceRepository tgVoiceRepository) {
        this.orderClientRepository = orderClientRepository;
        this.songRepository = songRepository;
        this.tgUserRepository = tgUserRepository;
        this.comparingService = comparingService;
        this.fileService = fileService;
        this.tgAudioRepository = tgAudioRepository;
        this.tgVoiceRepository = tgVoiceRepository;
    }

    public TgUser findOrSaveAppUser(org.telegram.telegrambots.meta.api.objects.User telegramUser) {
        //todo раскомментить после тестов
        TgUser persistentTgUser = tgUserRepository.findByTelegramUserId(telegramUser.getId());
//        TgUser persistentTgUser = null;
        if (persistentTgUser == null) {
            log.debug("NODE: NEW user in system, adding: " + telegramUser.getId());

            //Объект еще не представлен в бд и его предстоит сохранить
            TgUser transientTgUser = TgUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .userState(AWAITING_FOR_COMMAND)
//                    .userState(AWAITING_FOR_AUDIO)
                    .build();

            transientTgUser = tgUserRepository.save(transientTgUser);
            return addOrder(transientTgUser);
        }
        log.debug("NODE: User ALREADY in system");
        return persistentTgUser;
    }

    public TgUser findAppUsersByTelegramUserId(java.lang.Long id) {
        return tgUserRepository.findByTelegramUserId(id);
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
        TgUser tgUser = tgUserRepository.findByTelegramUserId(findTelegramUserIdFromUpdate(update).getId());
        tgUser.setUserState(userState);
        tgUserRepository.save(tgUser);
    }

    public String getClientName(Update update) {
        TgUser tgUser = tgUserRepository.findByTelegramUserId(findTelegramUserIdFromUpdate(update).getId());
        return tgUser.getNameAsClient();
    }

    public void setClientName(Update update, String clientName) {
        TgUser tgUser = tgUserRepository.findByTelegramUserId(findTelegramUserIdFromUpdate(update).getId());
        tgUser.setNameAsClient(clientName);
        tgUserRepository.save(tgUser);
    }


    public void updateSong(Update update, Song newSong) {
        TgUser tgUser = tgUserRepository.findByTelegramUserId(findTelegramUserIdFromUpdate(update).getId());
        Song updatable = songRepository.findSongById(tgUser.getCurrentSongId());
        songRepository.save(comparingService.songUpdate(newSong, updatable));
    }
//    public void updateSong(Song oldSong, Song newSong) {
//        Song updatable = songRepository.findSongById(tgUser.getCurrentSongId());
//        songRepository.save(comparingService.songUpdate(newSong, updatable));
//    }

    public TgUser addOrder(TgUser tgUser) {
        log.debug("BDS: adding new order");
        //todo учесть вариант если заказ не завершен, и его нужно продолжить обрабатывать

        List<OrderClient> orderList = orderClientRepository.findOrderClientsByTgUser(tgUser);
        orderList = hasUnacceptedOrders(orderList);

        if (!orderList.isEmpty()) {
            log.debug("BDS: This user have already unsuccessful order");
        } else {
            //todo написать логику создания нового ордера
            OrderClient orderClient = defaultOrder(tgUser);
            tgUser = orderClient.getTgUser();
            return tgUser;
        }
        //достать еще несуществующую коллекцию заказов присобаченную к юзеру. Заполнить ее пустой болванкой
        return null;
    }

    public OrderClient defaultOrder(TgUser tgUser) {
        log.debug("BDS: init order");

        OrderClient orderClient = OrderClient.builder()
                .tgUser(tgUser)
                .build();

        orderClient = orderClientRepository.save(orderClient);
        tgUser = findAppUsersByTelegramUserId(tgUser.getTelegramUserId());
        tgUser.setCurrentOrderId(orderClient.getId());
        tgUser = tgUserRepository.save(tgUser);

        Song song = songRepository.save(defaultSong(orderClient));

        tgUser = findAppUsersByTelegramUserId(tgUser.getTelegramUserId());
        tgUser.setCurrentSongId(song.getId());
        tgUser = tgUserRepository.save(tgUser);

        return orderClientRepository.findOrderClientById(tgUser.getCurrentOrderId());
    }

    public Song defaultSong(OrderClient orderClient) {
        return Song.builder()
                .orderClient(orderClient)
                .build();
    }


    public List<OrderClient> hasUnacceptedOrders(List<OrderClient> orderList) {
        return orderList.stream()
                .filter(order -> !order.isAccepted()) // isAccepted = false
                .collect(Collectors.toList());
    }

    public void addMp3(Update update) {
        TgAudio tgAudio = fileService.processAudio(update);
        tgAudio = tgAudioRepository.save(tgAudio);

        TgUser tgUser = findOrSaveAppUser(findTelegramUserIdFromUpdate(update));
        Song song = songRepository.findSongById(tgUser.getCurrentSongId());

        tgAudio.setSong(song);
        tgAudioRepository.save(tgAudio);
    }

    public void addVoice(Update update) {
        TgVoice tgVoice = fileService.processVoice(update);
        tgVoice = tgVoiceRepository.save(tgVoice);

        TgUser tgUser = findOrSaveAppUser(findTelegramUserIdFromUpdate(update));
        Song song = songRepository.findSongById(tgUser.getCurrentSongId());

        tgVoice.setSong(song);
        tgVoiceRepository.save(tgVoice);
    }
}




