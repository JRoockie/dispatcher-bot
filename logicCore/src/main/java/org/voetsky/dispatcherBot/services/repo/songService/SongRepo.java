package org.voetsky.dispatcherBot.services.repo.songService;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.WhoWillSing;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;

import java.util.List;

public interface SongRepo {

    Song findSongById(Long id);

    Song save(Song song);

    Song defaultSong(OrderClient orderClient);

    List<Song> findSongsByOrderClient(OrderClient orderClient);
}
