package org.voetsky.dispatcherBot.services.repo.tgAudioService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudioRepository;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;
import org.voetsky.dispatcherBot.services.repo.songService.SongRepositoryService;
import org.voetsky.dispatcherBot.services.repo.tgUserService.TgUserRepositoryService;
import org.voetsky.dispatcherBot.services.repoServices.fileService.FileService;

@AllArgsConstructor
@Service
public class TgAudioRepositoryService implements TgAudioRepo {
    private final TgAudioRepository tgAudioRepository;
    private final SongRepositoryService songRepositoryService;
    private final FileService fileService;
    private final TgUserRepositoryService tgUserRepositoryService;

    public void addMp3(Update update) {
        TgAudio tgAudio = fileService.processAudio(update);
        tgAudio = tgAudioRepository.save(tgAudio);

        TgUser tgUser = tgUserRepositoryService.findOrSaveAppUser(
                tgUserRepositoryService.findUserIdFromUpdate(update));

        Song song = songRepositoryService.findSongById(tgUser.getCurrentSongId());
        tgAudio.setSong(song);
        tgAudioRepository.save(tgAudio);
        songRepositoryService.save(song);
    }

}
