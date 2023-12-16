package org.voetsky.dispatcherBot.services.repo.tgVoiceService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoiceRepository;
import org.voetsky.dispatcherBot.services.repo.songService.SongRepositoryService;
import org.voetsky.dispatcherBot.services.repo.tgUserService.TgUserRepositoryService;
import org.voetsky.dispatcherBot.services.repoServices.fileService.FileService;

@Log4j
@AllArgsConstructor
@Service
public class TgVoiceRepositoryService implements TgVoiceRepo {

    private final TgVoiceRepository tgVoiceRepository;
    private final SongRepositoryService songRepositoryService;
    private final TgUserRepositoryService tgUserRepositoryService;
    private final FileService fileService;

    public void addVoice(Update update) {
        TgVoice tgVoice = fileService.processVoice(update);
        tgVoice = tgVoiceRepository.save(tgVoice);

        TgUser tgUser = tgUserRepositoryService.findOrSaveAppUser(
                tgUserRepositoryService.findUserIdFromUpdate(update));
        Song song = songRepositoryService.findSongById(tgUser.getCurrentSongId());

        tgVoice.setSong(song);
        tgVoiceRepository.save(tgVoice);
    }

}
