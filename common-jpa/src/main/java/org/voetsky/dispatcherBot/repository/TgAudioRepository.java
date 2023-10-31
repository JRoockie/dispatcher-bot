package org.voetsky.dispatcherBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entity.Song;
import org.voetsky.dispatcherBot.entity.TgAudio;

import java.util.List;

public interface TgAudioRepository extends JpaRepository<TgAudio, Long> {

    List<TgAudio> findBySong(Song song);

}
