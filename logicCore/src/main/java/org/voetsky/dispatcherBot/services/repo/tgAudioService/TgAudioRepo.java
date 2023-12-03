package org.voetsky.dispatcherBot.services.repo.tgAudioService;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TgAudioRepo {

   void addMp3(Update update);

}
