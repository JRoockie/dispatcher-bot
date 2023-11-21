package org.voetsky.dispatcherBot.services.interf;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.entity.TgAudio;
import org.voetsky.dispatcherBot.entity.TgVoice;

public interface FileService {
    TgAudio processAudio(Update update);
    TgVoice processVoice(Update update);
}
