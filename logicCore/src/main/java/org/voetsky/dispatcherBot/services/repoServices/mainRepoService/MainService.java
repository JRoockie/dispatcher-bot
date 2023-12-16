package org.voetsky.dispatcherBot.services.repoServices.mainRepoService;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;

public interface MainService {

    void setUserState(Update update, UserState userState);

    void updateUser(Update update, TgUser newTgUser);

    void addMp3(Update update);

    void addVoice(Update update);

    void addSong(Update update, Song song);

    void updateSong(Update update, Song song);

    void addOrder(Update update);

    void updateOrder(Update update, OrderClient newOrderClient);

    String getPrice(Update update, Long oneSingerPrice, Long oneSongPrice, String bill, Long discountLimit);

    void fillSongNullFields(Update update);

    TgUser getTgUserFromUpdate(Update update);
}
