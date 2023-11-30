package org.voetsky.dispatcherBot.services.repoAcess.songService;

import org.voetsky.dispatcherBot.entity.OrderClient;
import org.voetsky.dispatcherBot.entity.Song;

public interface SongRepo {

    Song findSongById(Long id);

    Song save(Song song);

    Song defaultSong(OrderClient orderClient);


}
