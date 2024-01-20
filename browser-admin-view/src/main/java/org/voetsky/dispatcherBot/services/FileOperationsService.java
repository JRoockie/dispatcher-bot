package org.voetsky.dispatcherBot.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.repository.song.SongRepository;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;

@AllArgsConstructor
@Log4j
@Service
public class FileOperationsService implements FileOperations {

    private final SongRepository songRepository;

    @Override
    public TgAudio getTgAudio(Long songId) {
        return songRepository.findSongById(songId).getTgAudio();
    }

    @Override
    public TgVoice getTgVoice(Long songId) {
        return songRepository.findSongById(songId).getTgVoice();
    }

}
