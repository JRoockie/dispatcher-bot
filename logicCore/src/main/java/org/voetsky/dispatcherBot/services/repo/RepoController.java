package org.voetsky.dispatcherBot.services.repo;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;
import org.voetsky.dispatcherBot.services.repo.orderClientService.OrderClientRepo;
import org.voetsky.dispatcherBot.services.repo.songService.SongRepo;
import org.voetsky.dispatcherBot.services.repo.tgAudioService.TgAudioRepo;
import org.voetsky.dispatcherBot.services.repo.tgUserService.TgUserRepo;
import org.voetsky.dispatcherBot.services.repo.tgVoiceService.TgVoiceRepo;
import org.voetsky.dispatcherBot.services.repoServices.comparingEntityService.ComparingEntityService;

@Log4j
@AllArgsConstructor
@Service
public class RepoController {

    private final OrderClientRepo orderClientRepo;
    private final SongRepo songRepo;
    private final TgUserRepo tgUserRepo;
    private final TgAudioRepo tgAudioRepo;
    private final TgVoiceRepo tgVoiceRepo;
    private final ComparingEntityService comparingEntityService;

    public User findTelegramUserIdFromUpdate(Update update) {
        return tgUserRepo.findUserIdFromUpdate(update);
    }

    public void setUserState(Update update, UserState userState) {
        tgUserRepo.setState(update, userState);
    }

    public void updateUser(Update update, TgUser newTgUser) {
        TgUser updatable = getTgUserFromUpdate(update);
        updatable = comparingEntityService.tgUserUpdate(newTgUser, updatable);
        tgUserRepo.save(updatable);
    }

    public void setClientName(Update update, String text) {
        tgUserRepo.setClientName(update, text);
    }

    public void addMp3(Update update) {
        tgAudioRepo.addMp3(update);
    }

    public String getClientName(Update update) {
        return tgUserRepo.getClientName(update);
    }

    public void addVoice(Update update) {
        tgVoiceRepo.addVoice(update);
    }

    public void addSong(Update update, Song song) {
        TgUser tgUser = getTgUserFromUpdate(update);

        OrderClient orderClient = orderClientRepo.findOrderClientById(
                tgUser.getCurrentOrderId());

        song.setOrderClient(orderClient);
        song.setSongName("-");
        Song song1 = songRepo.save(song);

        tgUserRepo.setCurrentSong(update, song1.getId());
    }

    public void updateSong(Update update, Song song) {
        TgUser tgUser = getTgUserFromUpdate(update);
        if (tgUser.getCurrentSongId() == null) {
            addSong(update, song);
            return;
        } else if (songRepo.findSongById(tgUser.getCurrentSongId()).isFilled()) {
            addSong(update, song);
        }
        Song original = songRepo.findSongById(tgUser.getCurrentSongId());
        original = comparingEntityService.songUpdate(song, original);
        songRepo.save(original);
    }

    public void addOrder(Update update) {
        TgUser tgUser = getTgUserFromUpdate(update);
        OrderClient orderClient = orderClientRepo.defaultOrder(tgUser);
        tgUserRepo.addOrderToTgUser(tgUser, orderClient);
    }

    public void updateOrder(Update update, OrderClient newOrderClient) {
        TgUser tgUser = getTgUserFromUpdate(update);
        OrderClient updatable = orderClientRepo.findOrderClientById(tgUser.getCurrentOrderId());
        OrderClient original = comparingEntityService.orderClientUpdate(updatable, newOrderClient);
        orderClientRepo.save(original);
    }

    public String getPrice(Update update, Long oneSingerPrice,
                           Long oneSongPrice, String bill, Long discountLimit) {

        TgUser tgUser = getTgUserFromUpdate(update);
        OrderClient order = orderClientRepo.findOrderClientById(tgUser.getCurrentOrderId());
        var songs = songRepo.findSongsByOrderClient(order);

        int singerCount = songs.stream().mapToInt(Song::getSingerCount).sum();
        int songCount = songs.size();
        double hoursCount = 1 + (singerCount - 1) * 0.5;
        long discount = (singerCount > discountLimit) ? singerCount * 1000L : 0;
        Long songPrice = oneSongPrice * songCount;
        Long singerPrice = oneSingerPrice * singerCount;
        Long score = songPrice + singerPrice - discount;
        bill = String.format(bill, singerCount, oneSingerPrice, singerPrice, hoursCount, songCount, songPrice, discount, score);
        return bill;
    }

    private TgUser getTgUserFromUpdate(Update update) {
        return tgUserRepo
                .findAppUsersByTelegramUserId(
                        tgUserRepo.getIdFromUpdate(update));
    }


}
