package org.voetsky.dispatcherBot.services.repoServices.comparingEntityService;

import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;

@Service
public interface ComparingEntity {

    OrderClient orderClientUpdate(OrderClient newOrder, OrderClient updatable);

    Song songUpdate(Song newSong, Song updatableSong);

    TgUser tgUserUpdate(TgUser newUser, TgUser updatableUser);

}
