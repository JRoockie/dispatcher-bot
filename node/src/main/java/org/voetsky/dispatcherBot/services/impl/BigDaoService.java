package org.voetsky.dispatcherBot.services.impl;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.entity.OrderClient;
import org.voetsky.dispatcherBot.entity.Song;
import org.voetsky.dispatcherBot.repository.OrderClientRepository;
import org.voetsky.dispatcherBot.repository.SongRepository;
import org.voetsky.dispatcherBot.repository.TgUserRepository;
import org.voetsky.dispatcherBot.entity.TgUser;

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

    public BigDaoService(OrderClientRepository orderClientRepository, SongRepository songRepository, TgUserRepository tgUserRepository, ComparingService comparingService) {
        this.orderClientRepository = orderClientRepository;
        this.songRepository = songRepository;
        this.tgUserRepository = tgUserRepository;
        this.comparingService = comparingService;
    }

    public TgUser findOrSaveAppUser(org.telegram.telegrambots.meta.api.objects.User telegramUser) {
        TgUser persistentTgUser = tgUserRepository.findAppUsersByTelegramUserId(telegramUser.getId());
        if (persistentTgUser == null) {
            log.debug("NODE: NEW user in system, adding: " + telegramUser.getId());

            //Объект еще не представлен в бд и его предстоит сохранить
            TgUser transientTgUser = TgUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .userState(AWAITING_FOR_COMMAND)
                    .build();
            return tgUserRepository.save(transientTgUser);
        }
        log.debug("NODE: User ALREADY in system");
        return persistentTgUser;
    }

    public TgUser findAppUsersByTelegramUserId(java.lang.Long id) {
        return tgUserRepository.findAppUsersByTelegramUserId(id);
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
        TgUser tgUser = tgUserRepository.findAppUsersByTelegramUserId(findTelegramUserIdFromUpdate(update).getId());
        tgUser.setUserState(userState);
        tgUserRepository.save(tgUser);
    }

    public String getClientName(Update update) {
        TgUser tgUser = tgUserRepository.findAppUsersByTelegramUserId(findTelegramUserIdFromUpdate(update).getId());
        return tgUser.getNameAsClient();
    }

    public void setClientName(Update update, String clientName) {
        TgUser tgUser = tgUserRepository.findAppUsersByTelegramUserId(findTelegramUserIdFromUpdate(update).getId());
        tgUser.setNameAsClient(clientName);
        tgUserRepository.save(tgUser);
    }


    public void updateSong(Update update, Song newSong){
        TgUser tgUser = tgUserRepository.findAppUsersByTelegramUserId(findTelegramUserIdFromUpdate(update).getId());
        Song updatable = songRepository.findSongById(tgUser.getCurrentSongId());
        songRepository.save(comparingService.songUpdate(newSong,updatable));
    }

    public void addOrder(Update update) {
        log.debug("BDS: adding new order");
        //todo учесть вариант если заказ не завершен, и его нужно продолжить обрабатывать

        TgUser tgUser = findOrSaveAppUser(findTelegramUserIdFromUpdate(update));

        List<OrderClient> orderList = getOrderClientList(update);
        orderList = hasUnacceptedOrders(orderList);

        if (!orderList.isEmpty()){
            log.debug("BDS: This user have already unsuccessful order");
        } else {
            OrderClient orderClient = OrderClient.builder()
                    .comment("FirstOrder")
                    .price("20000")
                    .tgUser(tgUser)
                    .build();
            orderClient = orderClientRepository.save(orderClient);
            tgUser.setCurrentOrderId(orderClient.getId());
            tgUser.setCurrentSongId(addSong(orderClient).getId());
            tgUserRepository.save(tgUser);
        }
        //достать еще несуществующую коллекцию заказов присобаченную к юзеру. Заполнить ее пустой болванкой

    }



    public Song addSong(OrderClient orderClient){
        Song song = Song.builder()
                .orderClient(orderClient)
                .build();
        return songRepository.save(song);
    }
    public List<OrderClient> getOrderClientList(Update update){
        TgUser tgUser = findOrSaveAppUser(findTelegramUserIdFromUpdate(update));
        return orderClientRepository.findOrderClientsByTgUser(tgUser);
    }

    public List<OrderClient> hasUnacceptedOrders(List<OrderClient> orderList){
        return orderList.stream()
                .filter(order -> !order.isAccepted()) // isAccepted = false
                .collect(Collectors.toList());
    }

}




