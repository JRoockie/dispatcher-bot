package org.voetsky.dispatcherBot.services.repoService.tgAudioService;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TgAudioRepo {

    public void addMp3(Update update);

}
