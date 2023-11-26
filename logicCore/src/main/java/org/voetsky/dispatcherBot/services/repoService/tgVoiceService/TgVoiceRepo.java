package org.voetsky.dispatcherBot.services.repoService.tgVoiceService;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TgVoiceRepo {

    void addVoice(Update update);

}
