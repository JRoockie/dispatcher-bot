package org.voetsky.dispatcherBot.services.repoServices.mainRepoService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;
import org.voetsky.dispatcherBot.services.repo.orderClientService.OrderClientRepo;
import org.voetsky.dispatcherBot.services.repo.songService.SongRepo;
import org.voetsky.dispatcherBot.services.repo.tgAudioService.TgAudioRepo;
import org.voetsky.dispatcherBot.services.repo.tgUserService.TgUserRepo;
import org.voetsky.dispatcherBot.services.repo.tgVoiceService.TgVoiceRepo;
import org.voetsky.dispatcherBot.services.repoServices.comparingEntityService.ComparingEntity;

@Log4j
@AllArgsConstructor
@Service
public class MainRepoService implements MainService {

    private final OrderClientRepo orderClientRepo;
    private final SongRepo songRepo;
    private final TgUserRepo tgUserRepo;
    private final TgAudioRepo tgAudioRepo;
    private final TgVoiceRepo tgVoiceRepo;
    private final ComparingEntity comparingEntity;

    @Override
    public void setUserState(Update update, UserState userState) {
        tgUserRepo.setState(update, userState);
    }

    @Override
    public void updateUser(Update update, TgUser newTgUser) {
        TgUser updatable = getTgUserFromUpdate(update);
        updatable = comparingEntity.tgUserUpdate(newTgUser, updatable);
        tgUserRepo.save(updatable);
    }

    @Override
    public void addMp3(Update update) {
        tgAudioRepo.addMp3(update);
    }

    @Override
    public void addVoice(Update update) {
        tgVoiceRepo.addVoice(update);
    }

    @Override
    public void addSong(Update update, Song song) {
        TgUser tgUser = getTgUserFromUpdate(update);

        OrderClient orderClient = orderClientRepo.findOrderClientById(
                tgUser.getCurrentOrderId());

        song.setOrderClient(orderClient);
        Song song1 = songRepo.save(song);

        tgUserRepo.setCurrentSong(update, song1.getId());
    }

    @Override
    public void updateSong(Update update, Song song) {
        TgUser tgUser = getTgUserFromUpdate(update);
        if (tgUser.getCurrentSongId() == null) {
            addSong(update, song);
            return;
        } else if (songRepo.findSongById(tgUser.getCurrentSongId()).isFilled()) {
            addSong(update, song);
            return;
        }
        Song original = songRepo.findSongById(tgUser.getCurrentSongId());
        original = comparingEntity.songUpdate(song, original);
        songRepo.save(original);
    }

    @Override
    public void addOrder(Update update) {
        TgUser tgUser = getTgUserFromUpdate(update);
        OrderClient orderClient = orderClientRepo.defaultOrder(tgUser);
        tgUserRepo.addOrderToTgUser(tgUser, orderClient);
    }

    @Override
    public void updateOrder(Update update, OrderClient newOrderClient) {
        TgUser tgUser = getTgUserFromUpdate(update);
        OrderClient original = orderClientRepo.findOrderClientById(tgUser.getCurrentOrderId());
        original = comparingEntity.orderClientUpdate(newOrderClient, original);
        orderClientRepo.save(original);
    }

    @Override
    public String getPrice(Update update, Long oneSingerPrice, Long oneSongPrice, String bill, Long discountLimit) {
        TgUser tgUser = getTgUserFromUpdate(update);
        OrderClient order = orderClientRepo.findOrderClientById(tgUser.getCurrentOrderId());
        var songs = songRepo.findSongsByOrderClient(order);

        int singerCount = songs.stream().mapToInt(Song::getSingerCount).sum();
        int songCount = songs.size();
        double hoursCount = calculateHoursCount(singerCount);
        long discount = calculateDiscount(singerCount, discountLimit);
        Long songPrice = oneSongPrice * songCount;
        Long singerPrice = oneSingerPrice * singerCount;
        Long score = calculateScore(songPrice, singerPrice, discount);

        bill = formatBill(
                bill,
                singerCount,
                oneSingerPrice,
                singerPrice,
                hoursCount,
                songCount,
                songPrice,
                discount,
                score);

        OrderClient orderClient = new OrderClient();
        orderClient.setPrice(String.valueOf(score));
        updateOrder(update, orderClient);

        return bill;
    }

    private double calculateHoursCount(int singerCount) {
        return 1 + (singerCount - 1) * 0.5;
    }

    private long calculateDiscount(int singerCount, Long discountLimit) {
        if (singerCount>= 5){
            return singerCount * 2000L;
        }
        return (singerCount > discountLimit) ? singerCount * 1000L : 0;
    }

    private Long calculateScore(Long songPrice, Long singerPrice, long discount) {
        return songPrice + singerPrice - discount;
    }

    private String formatBill(String bill, int singerCount, Long oneSingerPrice, Long singerPrice, double hoursCount,
                              int songCount, Long songPrice, long discount, Long score) {
        return String.format(bill, singerCount,
                oneSingerPrice, singerPrice,
                hoursCount, songCount,
                songPrice, discount, score);
    }

    @Override
    public TgUser getTgUserFromUpdate(Update update) {
        return tgUserRepo
                .findAppUsersByTelegramUserId(
                        tgUserRepo.getIdFromUpdate(update));
    }

    @Override
    public void fillSongNullFields(Update update) {
        TgUser tgUser = getTgUserFromUpdate(update);
        Song song = songRepo.findSongById(tgUser.getCurrentSongId());
        if (song.getLink() == null) {
            song.setLink("");
        }
        if (song.getSongName() == null) {
            song.setSongName("");
        }
        songRepo.save(song);
    }

}
