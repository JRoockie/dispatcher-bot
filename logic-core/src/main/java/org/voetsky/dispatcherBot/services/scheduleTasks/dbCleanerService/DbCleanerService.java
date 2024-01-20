package org.voetsky.dispatcherBot.services.scheduleTasks.dbCleanerService;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMaker;
import org.voetsky.dispatcherBot.services.repo.orderClientService.OrderClientRepo;
import org.voetsky.dispatcherBot.services.repo.songService.SongRepo;
import org.voetsky.dispatcherBot.services.repo.tgAudioService.TgAudioRepo;
import org.voetsky.dispatcherBot.services.repo.tgVoiceService.TgVoiceRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Log4j
public class DbCleanerService implements DbCleaner {
    private final OrderClientRepo orderClientRepository;
    private final SongRepo songRepository;
    private final TgAudioRepo tgAudioRepository;
    private final TgVoiceRepo tgVoiceRepository;
    private final MessageMaker messageMaker;

    @Override
    @Scheduled(cron = "@weekly")
    public void clearDB(){
        if (log.isDebugEnabled()) {
            log.debug("Начинаю очистку баз данных");
        }

        List<OrderClient> orderClients = orderClientRepository.findOrderClientsByIsAcceptedFalse();
        LocalDateTime today = LocalDateTime.now();
        Long differenceDays = 7L;

        orderClients = removeOrderClients(orderClients, today, differenceDays);
        List<Song> songs = removeSongs(orderClients, today);
        List<TgVoice> tgVoices = removeTgVoices(songs, today);
        List<TgAudio> tgAudios = removeTgAudios(songs, today);

        if (log.isDebugEnabled()) {
            log.debug("Текущее время: " + today);

            int orderRes = orderClients.size();
            int songsRes = songs.size();
            int tgAudiosRes = tgAudios.size();
            int tgVoiceRes = tgVoices.size();

            String debugInfo = messageMaker.getTextFromProperties("db.clean.info");
            log.debug(String.format(debugInfo,
                    orderRes, songsRes, tgVoiceRes, tgAudiosRes));
        }

    }

    private List<OrderClient> removeOrderClients(List<OrderClient> orderClients, LocalDateTime today, Long difference) {
        orderClients = orderClients.stream()
                .filter(Objects::nonNull)
                .filter(x -> Objects.isNull(x.getDeletedWhen()))
                .filter(x -> x.getDate().plusDays(difference).isBefore(today))
                .collect(Collectors.toList());

        if (!orderClients.isEmpty()) {
            orderClients.forEach(x -> x.setDeletedWhen(today));
            orderClients.forEach(x -> x.setIsAccepted(false));
            orderClientRepository.save(orderClients);

            if (log.isDebugEnabled()) {
                orderClients.forEach(x -> log.debug("Заказ удален: ID " + x.getId()));
            }
        }
        return orderClients;
    }


    private List<TgVoice> removeTgVoices(List<Song> songs, LocalDateTime today) {
        List<TgVoice> tgVoices = songs.stream()
                .map(Song::getTgVoice)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!tgVoices.isEmpty()) {
            tgVoices.forEach(x -> x.setDeletedWhen(today));
            tgVoiceRepository.save(tgVoices);

            if (log.isDebugEnabled()) {
                tgVoices.forEach(x -> log.debug("ГС удалено: ID " + x.getId()));
            }
        }
        return tgVoices;
    }

    private List<TgAudio> removeTgAudios(List<Song> songs, LocalDateTime today) {
        List<TgAudio> tgAudios = songs.stream()
                .map(Song::getTgAudio)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!tgAudios.isEmpty()) {
            tgAudios.forEach(x -> x.setDeletedWhen(today));
            tgAudioRepository.save(tgAudios);

            if (log.isDebugEnabled()) {
                tgAudios.forEach(x -> log.debug("MP3 удалено: ID " + x.getId()));
            }
        }
        return tgAudios;
    }

    private List<Song> removeSongs(List<OrderClient> orderClients, LocalDateTime today) {
        List<Song> songs = orderClients.stream()
                .flatMap(orderClient -> orderClient.getSongs().stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!songs.isEmpty()) {
            songs.forEach(x -> x.setDeletedWhen(today));
            songRepository.save(songs);

            if (log.isDebugEnabled()) {
                songs.forEach(x -> log.debug("Песня удалена: ID " + x.getId()));
            }
        }
        return songs;
    }


}
