package org.voetsky.dispatcherBot.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.entity.OrderClient;
import org.voetsky.dispatcherBot.entity.Song;
import org.voetsky.dispatcherBot.entity.TgUser;
import org.voetsky.dispatcherBot.services.comparingEntityService.ComparingEntityService;
import org.voetsky.dispatcherBot.services.repoService.orderClientService.OrderClientRepo;
import org.voetsky.dispatcherBot.services.repoService.songService.SongRepo;
import org.voetsky.dispatcherBot.services.repoService.tgAudioService.TgAudioRepo;
import org.voetsky.dispatcherBot.services.repoService.tgUserService.TgUserRepo;
import org.voetsky.dispatcherBot.services.repoService.tgVoiceService.TgVoiceRepo;

@Log4j
@Component
@AllArgsConstructor
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

    public void setClientName(Update update, String text) {
        tgUserRepo.setClientName(update, text);
    }

    public void addMp3(Update update) {
        tgVoiceRepo.addVoice(update);
    }

    public String getClientName(Update update) {
        return tgUserRepo.getClientName(update);
    }

    public void addVoice(Update update) {
        tgAudioRepo.addMp3(update);
    }

    public void addSong(Update update, Song song) {
        TgUser tgUser = tgUserRepo
                .findAppUsersByTelegramUserId(
                        tgUserRepo.getIdFromUpdate(update));

        OrderClient orderClient = orderClientRepo.findOrderClientById(
                tgUser.getCurrentOrderId());

        song.setOrderClient(orderClient);
        Song song1 = songRepo.save(song);

        tgUserRepo.setCurrentSong(update, song1.getId());
    }

    public void updateSong(Update update, Song song) {
        TgUser tgUser = tgUserRepo
                .findAppUsersByTelegramUserId(
                        tgUserRepo.getIdFromUpdate(update));

        OrderClient orderClient = orderClientRepo.findOrderClientById(
                tgUser.getCurrentOrderId());

        Song originalSong = songRepo.findSongById(tgUser.getCurrentSongId());
        originalSong = comparingEntityService.songUpdate(song, originalSong);

        songRepo.save(originalSong);
    }

    public void addOrder(Update update) {
        TgUser tgUser = tgUserRepo
                .findAppUsersByTelegramUserId(
                        tgUserRepo.getIdFromUpdate(update));

        OrderClient orderClient = orderClientRepo.defaultOrder(tgUser);
        tgUserRepo.addOrderToTgUser(tgUser, orderClient);
    }
}
