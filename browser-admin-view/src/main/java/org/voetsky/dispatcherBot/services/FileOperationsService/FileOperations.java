package org.voetsky.dispatcherBot.services.FileOperationsService;

import jakarta.servlet.http.HttpServletResponse;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;

public interface FileOperations {

    TgAudio getTgAudio(Long songId);

    TgVoice getTgVoice(Long songId);

    void downloadAudio(Long id, HttpServletResponse response);

    void downloadVoice(Long id, HttpServletResponse response);
}
