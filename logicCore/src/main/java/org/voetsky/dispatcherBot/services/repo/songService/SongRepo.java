package org.voetsky.dispatcherBot.services.repo.songService;

import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;

import java.util.List;

public interface SongRepo {

    Song findSongById(Long id);

    Song save(Song song);

    List<Song> findSongsByOrderClient(OrderClient orderClient);
}
