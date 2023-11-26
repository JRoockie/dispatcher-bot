package org.voetsky.dispatcherBot.services.repoService.songService;

import org.springframework.stereotype.Component;
import org.voetsky.dispatcherBot.entity.OrderClient;
import org.voetsky.dispatcherBot.entity.Song;
import org.voetsky.dispatcherBot.repository.SongRepository;
import org.voetsky.dispatcherBot.repository.TgUserRepository;



@Component
public class SongRepositoryService implements SongRepo{

    private final SongRepository songRepository;
    private final TgUserRepository tgUserRepository;

    public SongRepositoryService(SongRepository songRepository, TgUserRepository tgUserRepository) {
        this.songRepository = songRepository;
        this.tgUserRepository = tgUserRepository;
    }

    public Song findSongById(Long id){
        return songRepository.findSongById(id);
    }

    public Song save(Song song){
        return songRepository.save(song);
    }

    public Song defaultSong(OrderClient orderClient) {
        return Song.builder()
                .orderClient(orderClient)
                .isFilled(false)
                .build();
    }





}
