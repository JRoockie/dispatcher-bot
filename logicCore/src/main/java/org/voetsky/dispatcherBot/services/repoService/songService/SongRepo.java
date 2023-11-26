package org.voetsky.dispatcherBot.services.repoService.songService;

import org.voetsky.dispatcherBot.entity.OrderClient;
import org.voetsky.dispatcherBot.entity.Song;

public interface SongRepo {

    public Song findSongById(Long id);

    public Song save(Song song);

    public Song defaultSong(OrderClient orderClient);

}
