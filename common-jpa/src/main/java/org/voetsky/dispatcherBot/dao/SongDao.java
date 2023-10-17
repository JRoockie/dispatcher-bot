package org.voetsky.dispatcherBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entity.Song;

public interface SongDao extends JpaRepository<Song, Long> {
}
