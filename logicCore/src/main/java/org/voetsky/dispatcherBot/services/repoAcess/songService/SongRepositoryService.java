package org.voetsky.dispatcherBot.services.repoAcess.songService;

import org.springframework.stereotype.Component;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.song.SongRepository;
import org.voetsky.dispatcherBot.repository.tgUser.TgUserRepository;

@Component
public class SongRepositoryService implements SongRepo {

    private final SongRepository songRepository;

    public SongRepositoryService(SongRepository songRepository, TgUserRepository tgUserRepository) {
        this.songRepository = songRepository;
    }

    public Song findSongById(Long id) {
        return songRepository.findSongById(id);
    }

    public Song save(Song song) {
        return songRepository.save(song);
    }

    public Song defaultSong(OrderClient orderClient) {
        return Song.builder()
                .orderClient(orderClient)
                .isFilled(false)
                .build();
    }

}
