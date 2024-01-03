package org.voetsky.dispatcherBot.services.repo.tgVoiceService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoiceRepository;
import org.voetsky.dispatcherBot.services.repo.songService.SongRepo;
import org.voetsky.dispatcherBot.services.repo.tgUserService.TgUserRepo;
import org.voetsky.dispatcherBot.services.repoServices.fileService.FileOperations;

import java.util.List;

@Log4j
@AllArgsConstructor
@Service
public class TgVoiceRepositoryService implements TgVoiceRepo {

    private final TgVoiceRepository tgVoiceRepositoryJpa;
    private final SongRepo songRepository;
    private final TgUserRepo tgUserRepository;
    private final FileOperations fileOperations;

    public void addVoice(Update update) {
        TgVoice tgVoice = fileOperations.processVoice(update);
        tgVoice = tgVoiceRepositoryJpa.save(tgVoice);

        TgUser tgUser = tgUserRepository.findOrSaveAppUser(
                tgUserRepository.findUserIdFromUpdate(update));
        Song song = songRepository.findSongById(tgUser.getCurrentSongId());

        tgVoice.setSong(song);
        tgVoice = tgVoiceRepositoryJpa.save(tgVoice);

        song.setTgVoice(tgVoice);
        songRepository.save(song);
    }

    @Override
    public void save(List<TgVoice> tgVoices) {
        tgVoiceRepositoryJpa.saveAll(tgVoices);
    }

    @Override
    public TgVoice save(TgVoice tgVoice) {
        return tgVoiceRepositoryJpa.save(tgVoice);
    }

}
