package org.voetsky.dispatcherBot.services.repoAcess.comparingEntityService;

import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.entity.Song;
import org.voetsky.dispatcherBot.entity.TgUser;

@Service
public interface ComparingEntityService {

    Song songUpdate(Song newSong, Song updatableSong);

    TgUser tgUserUpdate(TgUser newUser, TgUser updatableUser);

}
