package org.voetsky.dispatcherBot.testDataFiller;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.WhoWillSing;
import org.voetsky.dispatcherBot.repository.binaryContent.BinaryContent;
import org.voetsky.dispatcherBot.repository.binaryContent.BinaryContentRepository;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;
import org.voetsky.dispatcherBot.services.repo.orderClientService.OrderClientRepo;
import org.voetsky.dispatcherBot.services.repo.songService.SongRepo;
import org.voetsky.dispatcherBot.services.repo.tgAudioService.TgAudioRepo;
import org.voetsky.dispatcherBot.services.repo.tgUserService.TgUserRepo;
import org.voetsky.dispatcherBot.services.repo.tgVoiceService.TgVoiceRepo;

import javax.xml.bind.DatatypeConverter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataFiller {

    private final TgUserRepo tgUserRepo;
    private final OrderClientRepo orderClientRepo;
    private final SongRepo songRepo;
    private final TgAudioRepo tgAudioRepo;
    private final TgVoiceRepo tgVoiceRepo;
    private final BinaryContentRepository binaryContentRepo;
    Faker faker = new Faker();

    private final int ordersAccepted = 40;
    private final int ordersSuccessful = 20;
    private final int ordersNotSuccess = 15;
    private final int ordersDeleted = 5;
    private int songsCount = 0;
    private final int orders = 100;
    int songs = 300;
    private final int songHasAudio = 50;

    public DataFiller(TgUserRepo tgUserRepo, OrderClientRepo orderClientRepo, SongRepo songRepo, TgAudioRepo tgAudioRepo, TgVoiceRepo tgVoiceRepo, BinaryContentRepository binaryContentRepo) {
        this.tgUserRepo = tgUserRepo;
        this.orderClientRepo = orderClientRepo;
        this.songRepo = songRepo;
        this.tgAudioRepo = tgAudioRepo;
        this.tgVoiceRepo = tgVoiceRepo;
        this.binaryContentRepo = binaryContentRepo;
    }

//    @PostConstruct
    synchronized public void fillData() {
        for (int i = 0; i <= orders; i++) {
            OrderClient order;
            TgUser tgUser = makeTgUser();

            if (i < orders) {
                if (i < ordersDeleted) {
                    order = makeOrder(tgUser, true, true, true);
                    fillSongs(order, true);

                } else if (i < ordersNotSuccess) {
                    order = makeOrder(tgUser, true, false, false);
                    fillSongs(order, false);

                } else if (i < ordersSuccessful) {
                    order = makeOrder(tgUser, true, true, false);
                    fillSongs(order, false);

                } else if (i < ordersAccepted) {
                    order = makeOrder(tgUser, false, true, false);
                    fillSongs(order, false);
                }
            }
        }

    }

    @PostConstruct
    public void bluntFrontendCrutch(){
        OrderClient order;
        OrderClient order1;
        TgUser tgUser = makeTgUser();
        order = makeOrder(tgUser, true, true, true);
        order1 = makeOrder(tgUser, true, false, true);
        fillSongs(order, true);
        fillSongs(order1, true);


    }

    public TgUser makeTgUser() {
        return tgUserRepo.save(TgUser.builder()
                .currentOrderId(1L)
                .currentSongId(1L)
                .firstLoginDate(LocalDateTime.now())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .localization("ru")
                .telegramUserId(1L)
                .userState(UserState.AWAITING_FOR_COMMAND)
                .username(faker.name().username())
                .build());
    }

    public OrderClient makeOrder(TgUser tgUser, boolean isAccepted, boolean successful, boolean deleted) {
        LocalDateTime localDateTime = null;
        if (deleted) {
            localDateTime = LocalDateTime.now();
        }
        return orderClientRepo.save(OrderClient.builder()
                .tgUser(tgUser)
                .comment(faker.lorem().paragraph(2))
                .date(localDateTime)
                .deletedWhen(localDateTime)
                .isAccepted(isAccepted)
                .nameAsClient(faker.name().firstName())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .price(String.valueOf(faker.number().numberBetween(8000, 100000)))
                .successful(successful)
                .build());
    }

    public void fillSongs(OrderClient order, boolean deleted) {
            if (songsCount < songHasAudio) {
                int count = ThreadLocalRandom.current().nextInt(1, 6);
                makeSong(order, deleted, true, count);
            } else if (songsCount < songs) {
                int count = ThreadLocalRandom.current().nextInt(1, 6);
                makeSong(order, deleted, false, count);
        }
    }

    public void makeSong(OrderClient orderClient, Boolean deleted, boolean hasAudio, int count) {
        List<Song> songList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            songsCount++;
            LocalDateTime localDateTime = null;
            TgAudio tgAudio = null;
            TgVoice tgVoice = makeTgVoice(deleted);

            if (deleted) {
                localDateTime = LocalDateTime.now();
            }
            if (hasAudio) {
                tgAudio = makeTgAudio(deleted);
            }
            Song song = Song.builder()
                    .tgAudio(tgAudio)
                    .tgVoice(tgVoice)
                    .orderClient(orderClient)
                    .deletedWhen(localDateTime)
                    .hasAudio(hasAudio)
                    .isFilled(true)
                    .link(faker.internet().domainName())
                    .singerCount(faker.number().numberBetween(1, 15))
                    .songName(faker.lorem().sentence())
                    .whoWillSing(WhoWillSing.ADULT_AND_CHILD)
                    .build();

            if (hasAudio) {
                song.setTgAudio(tgAudio);
            }
            songRepo.save(song);
            // если до этого сущности сохранить в бд в методе makeTgVoice то будет ошибка с detached persistent
            tgVoice.setSong(song);
            tgVoiceRepo.save(tgVoice);
            if (tgAudio != null) {
                tgAudio.setSong(song);
                tgAudioRepo.save(tgAudio);
            }
            songList.add(song);
        }
        orderClient.setSongs(songList);
        orderClientRepo.save(orderClient);
    }

    public TgVoice makeTgVoice(Boolean deleted) {
        LocalDateTime localDateTime = null;

        if (deleted) {
            localDateTime = LocalDateTime.now();
        }

        BinaryContent binaryContent = makeContent(deleted);

        TgVoice tgVoice = TgVoice.builder()
                .binaryContent(binaryContent)
                .deletedWhen(localDateTime)
                .fileSize((long) faker.number().numberBetween(200, 500))
                .mimeType("audio/ogg")
                .telegramFileId(String.valueOf(faker.number().numberBetween(0L, 99999L)))
                .build();
        binaryContentRepo.save(binaryContent);
        return tgVoice;
    }

    public TgAudio makeTgAudio(Boolean deleted) {
        LocalDateTime localDateTime = null;

        if (deleted) {
            localDateTime = LocalDateTime.now();
        }
        BinaryContent binaryContent = makeContent(deleted);

        return TgAudio.builder()
                .telegramFileId(String.valueOf(faker.number().numberBetween(0L, 99999L)))
                .audioName(faker.file().fileName())
                .date(LocalDateTime.now())
                .fileSize((long) faker.number().numberBetween(200, 500))
                .mimeType("audio/ogg")
                .binaryContent(binaryContent)
                .deletedWhen(localDateTime)
                .build();
    }

    @Transactional
    public BinaryContent makeContent(boolean deleted) {
        LocalDateTime localDateTime = null;

        if (deleted) {
            localDateTime = LocalDateTime.now();
        }
        String hexString = "4F6767530002000000000000";
        byte[] bytes = DatatypeConverter.parseHexBinary(hexString);

        return binaryContentRepo.save(BinaryContent.builder()
                .fileAsArrayOfBytes(bytes)
                .deletedWhen(localDateTime)
                .build());
    }

}
