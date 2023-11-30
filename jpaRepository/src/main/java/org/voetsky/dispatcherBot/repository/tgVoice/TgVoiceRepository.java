package org.voetsky.dispatcherBot.repository.tgVoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;

import java.util.List;

public interface TgVoiceRepository extends JpaRepository<TgVoice, Long> {
    List<TgVoice> findBySong(Song song);
}
