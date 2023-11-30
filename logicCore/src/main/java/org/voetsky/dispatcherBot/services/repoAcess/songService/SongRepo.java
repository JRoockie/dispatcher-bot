package org.voetsky.dispatcherBot.services.repoAcess.songService;

import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;

public interface SongRepo {

    Song findSongById(Long id);

    Song save(Song song);

    Song defaultSong(OrderClient orderClient);


}
