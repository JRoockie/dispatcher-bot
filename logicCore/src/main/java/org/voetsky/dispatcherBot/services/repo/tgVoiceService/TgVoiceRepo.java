package org.voetsky.dispatcherBot.services.repo.tgVoiceService;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;

import java.util.List;

public interface TgVoiceRepo {

    void addVoice(Update update);

    void save(List<TgVoice> tgVoices);

}
