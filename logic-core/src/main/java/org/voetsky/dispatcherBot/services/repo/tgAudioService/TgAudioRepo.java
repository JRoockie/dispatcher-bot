package org.voetsky.dispatcherBot.services.repo.tgAudioService;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;

import java.util.List;

public interface TgAudioRepo {

   void addMp3(Update update);

   void save(List<TgAudio> tgAudios);


    TgAudio save(TgAudio tgAudio);
}
