package org.voetsky.dispatcherBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entity.Song;
import org.voetsky.dispatcherBot.entity.TgAudio;
import org.voetsky.dispatcherBot.entity.TgVoice;

import java.util.List;

public interface TgVoiceRepository extends JpaRepository<TgVoice, Long> {
    List<TgVoice> findBySong(Song song);
}
