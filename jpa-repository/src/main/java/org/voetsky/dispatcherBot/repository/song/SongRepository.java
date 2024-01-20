package org.voetsky.dispatcherBot.repository.song;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    Song findSongById(Long id);

    List<Song> findSongsByOrderClient(OrderClient orderClient);

}
