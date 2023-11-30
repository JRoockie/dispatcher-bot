package org.voetsky.dispatcherBot.repository.tgAudio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;

import java.util.List;

public interface TgAudioRepository extends JpaRepository<TgAudio, Long> {

    List<TgAudio> findBySong(Song song);

}
