package org.voetsky.dispatcherBot.services.repo.tgAudioService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudioRepository;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;
import org.voetsky.dispatcherBot.services.repo.songService.SongRepo;
import org.voetsky.dispatcherBot.services.repo.tgUserService.TgUserRepo;
import org.voetsky.dispatcherBot.services.repoServices.fileService.FileOperations;

import java.util.List;

@AllArgsConstructor
@Service
public class TgAudioRepositoryService implements TgAudioRepo {
    private final TgAudioRepository tgAudioRepositoryJpa;
    private final SongRepo songRepositoryService;
    private final FileOperations fileOperations;
    private final TgUserRepo tgUserRepositoryService;

    public void addMp3(Update update) {
        TgAudio tgAudio = fileOperations.processAudio(update);
        tgAudio = tgAudioRepositoryJpa.save(tgAudio);

        TgUser tgUser = tgUserRepositoryService.findOrSaveAppUser(
                tgUserRepositoryService.findUserIdFromUpdate(update));

        Song song = songRepositoryService.findSongById(tgUser.getCurrentSongId());
        tgAudio.setSong(song);
        tgAudio = tgAudioRepositoryJpa.save(tgAudio);

        song.setTgAudio(tgAudio);
        song.setHasAudio(true);
        songRepositoryService.save(song);
    }

    @Override
    public void save(List<TgAudio> tgAudios) {
        tgAudioRepositoryJpa.saveAll(tgAudios);
    }

}
