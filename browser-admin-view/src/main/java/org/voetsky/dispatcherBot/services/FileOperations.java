package org.voetsky.dispatcherBot.services;

import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;

public interface FileOperations {

    TgAudio getTgAudio(Long songId);

    TgVoice getTgVoice(Long songId);

}
