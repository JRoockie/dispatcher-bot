package org.voetsky.dispatcherBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entity.OrderClient;
import org.voetsky.dispatcherBot.entity.Song;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    Song findSongById(Long id);

    List<Song> findSongsByOrderClient(OrderClient orderClient);

}
