package org.voetsky.dispatcherBot.repository.tgAudio;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TgAudioRepository extends JpaRepository<TgAudio, Long> {

    TgAudio findTgAudioById(Long id);

}
