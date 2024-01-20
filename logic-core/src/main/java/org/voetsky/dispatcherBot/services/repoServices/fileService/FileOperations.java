package org.voetsky.dispatcherBot.services.repoServices.fileService;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;

public interface FileOperations {

    TgAudio processAudio(Update update);

    TgVoice processVoice(Update update);

}
