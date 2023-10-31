package org.voetsky.dispatcherBot.services.impl;

import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.entity.Song;
import org.voetsky.dispatcherBot.entity.TgUser;

@Service
public class ComparingService {

    public Song songUpdate(Song newSong, Song updatableSong){

        if (newSong.getSongName() != null){
            updatableSong.setSongName(newSong.getSongName());
        }
        if (newSong.getLink() != null){
            updatableSong.setLink(newSong.getLink());
        }
        if (newSong.getSingerCount() != null){
            updatableSong.setSingerCount(newSong.getSingerCount());
        }
        if (newSong.getTgVoices() != null){
            updatableSong.setTgVoices(newSong.getTgVoices());
        }
        if (newSong.getTgAudios() != null){
            updatableSong.setTgAudios(newSong.getTgAudios());
        }
        return updatableSong;
    }

    public TgUser tgUserUpdate(TgUser newUser, TgUser updatableUser){
        if (newUser.getCurrentOrderId() != null){
            updatableUser.setCurrentOrderId(newUser.getCurrentOrderId());
        }
        if (newUser.getCurrentSongId() != null){
            updatableUser.setCurrentSongId(newUser.getCurrentSongId());
        }
        if (newUser.getUserState() != null){
            updatableUser.setUserState(newUser.getUserState());
        }
        return updatableUser;
    }



}
