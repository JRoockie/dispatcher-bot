package org.voetsky.dispatcherBot.services.repo.songService;

import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.song.SongRepository;

import java.util.List;

@Service
public class SongRepositoryService implements SongRepo {

    private final SongRepository songRepository;

    public SongRepositoryService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song findSongById(Long id) {
        return songRepository.findSongById(id);
    }

    public Song save(Song song) {
        return songRepository.save(song);
    }

    @Override
    public List<Song> findSongsByOrderClient(OrderClient orderClient) {
        return songRepository.findSongsByOrderClient(orderClient);
    }

}
