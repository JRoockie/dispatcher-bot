package org.voetsky.dispatcherBot.services.repoServices.comparingEntityService;

import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;

@Service
public class ComparingEntityServiceImpl implements ComparingEntityService {

    public OrderClient orderClientUpdate(
            OrderClient newOrder, OrderClient updatable) {

        if (newOrder.getComment() != null) {
            updatable.setComment(newOrder.getComment());
        }
        if (newOrder.getPrice() != null) {
            updatable.setPrice(newOrder.getPrice());
        }
        if (newOrder.isAccepted()) {
            updatable.setAccepted(true);
        }
        if (newOrder.getPhoneNumber() != null) {
            updatable.setPhoneNumber(newOrder.getPhoneNumber());
        }
        return updatable;
    }


    public Song songUpdate(Song newSong, Song updatable) {

        if (newSong.getTgAudios() != null) {
            updatable.setTgAudios(newSong.getTgAudios());
        }
        if (newSong.getTgVoices() != null) {
            updatable.setTgVoices(newSong.getTgVoices());
        }
        if (newSong.getSongName() != null) {
            updatable.setSongName(newSong.getSongName());
        }
        if (newSong.getLink() != null) {
            updatable.setLink(newSong.getLink());
        }
        if (newSong.getSingerCount() != null) {
            updatable.setSingerCount(newSong.getSingerCount());
        }
        if (newSong.isFilled()) {
            updatable.setFilled(true);
        }
        if (newSong.getWhoWillSing()!= null){
            updatable.setWhoWillSing(newSong.getWhoWillSing());
        }
        return updatable;
    }

    public TgUser tgUserUpdate(TgUser newUser, TgUser updatable) {
        if (newUser.getOrderList() != null) {
            updatable.setOrderList(newUser.getOrderList());
        }
        if (newUser.getNameAsClient() != null) {
            updatable.setNameAsClient(newUser.getNameAsClient());
        }
        if (newUser.getCurrentOrderId() != null) {
            updatable.setCurrentOrderId(newUser.getCurrentOrderId());
        }
        if (newUser.getCurrentSongId() != null) {
            updatable.setCurrentSongId(newUser.getCurrentSongId());
        }
        if (newUser.getUserState() != null) {
            updatable.setUserState(newUser.getUserState());
        }
        return updatable;
    }


}
