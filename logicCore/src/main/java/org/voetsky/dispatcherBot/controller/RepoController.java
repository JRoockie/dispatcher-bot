package org.voetsky.dispatcherBot.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.entity.Song;
import org.voetsky.dispatcherBot.entity.TgUser;
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

    public User findTelegramUserIdFromUpdate(Update update) {
        return tgUserRepo.findUserIdFromUpdate(update);
    }

    public void setUserState(Update update, UserState userState) {
        tgUserRepo.setState(update, userState);
    }

    public void setClientName(Update update, String text) {

    }

    public void addMp3(Update update) {

    }

    public String getClientName(Update update) {
        return "";
    }

    public void updateSong(Update update, Song newSong) {

    }

    public void addVoice(Update update) {
    }

    public void addDefaultOrder(Update update) {
        TgUser tgUser = tgUserRepo
                .findAppUsersByTelegramUserId(
                        tgUserRepo.getIdFromUpdate(update));

    }
}
