package org.voetsky.dispatcherBot.repository.tgVoice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TgVoiceRepository extends JpaRepository<TgVoice, Long> {

    TgVoice findTgVoiceById(Long id);

}
