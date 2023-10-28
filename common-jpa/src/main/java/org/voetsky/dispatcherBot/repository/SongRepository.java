package org.voetsky.dispatcherBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entity.Song;

public interface SongRepository extends JpaRepository<Song, Long> {
    Song findSongById(Long id);
}
