package org.voetsky.dispatcherBot.services.repo.songService;

import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.song.SongRepository;

import java.util.List;

@Service
public class SongRepositoryService implements SongRepo {

    private final SongRepository songRepositoryJpa;

    public SongRepositoryService(SongRepository songRepository) {
        this.songRepositoryJpa = songRepository;
    }

    public Song findSongById(Long id) {
        return songRepositoryJpa.findSongById(id);
    }

    public Song save(Song song) {
        return songRepositoryJpa.save(song);
    }

    @Override
    public List<Song> findSongsByOrderClient(OrderClient orderClient) {
        return songRepositoryJpa.findSongsByOrderClient(orderClient);
    }

    @Override
    public void save(List<Song> songs) {
        songRepositoryJpa.saveAll(songs);
    }

}
