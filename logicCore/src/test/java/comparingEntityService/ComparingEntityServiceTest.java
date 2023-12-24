package comparingEntityService;

import org.junit.jupiter.api.Test;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.WhoWillSing;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;
import org.voetsky.dispatcherBot.services.repoServices.comparingEntityService.ComparingEntity;
import org.voetsky.dispatcherBot.services.repoServices.comparingEntityService.ComparingEntityService;

import static junit.framework.Assert.assertEquals;

class ComparingEntityServiceTest {

    private ComparingEntity comparingEntityService;

    @Test
    void testOrderClientUpdate() {
        comparingEntityService = new ComparingEntityService();
        OrderClient existingOrder = new OrderClient();
        OrderClient newOrder = new OrderClient();

        newOrder.setPrice("2323223");
        newOrder.setComment("t");
        newOrder.setIsAccepted(true);
        newOrder.setPhoneNumber("341134");

        OrderClient result = comparingEntityService.orderClientUpdate(newOrder, existingOrder);

        assertEquals(newOrder.getComment(), result.getComment());
        assertEquals(newOrder.getPrice(), result.getPrice());
        assertEquals(newOrder.getIsAccepted(), result.getIsAccepted());
        assertEquals(newOrder.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    void testSongUpdate() {
        comparingEntityService = new ComparingEntityService();
        Song existingSong = new Song();
        Song newSong = new Song();

        newSong.setSongName("1");
        newSong.setTgAudio(new TgAudio());
        newSong.setTgVoice(new TgVoice());
        newSong.setLink("1");
        newSong.setSingerCount(2);
        newSong.setFilled(true);
        newSong.setWhoWillSing(WhoWillSing.CHILD);

        Song result = comparingEntityService.songUpdate(newSong, existingSong);

        assertEquals(newSong.getTgAudio(), result.getTgAudio());
        assertEquals(newSong.getTgVoice(), result.getTgVoice());
        assertEquals(newSong.getSongName(), result.getSongName());
        assertEquals(newSong.getLink(), result.getLink());
        assertEquals(newSong.getSingerCount(), result.getSingerCount());
        assertEquals(newSong.isFilled(), result.isFilled());
        assertEquals(newSong.getWhoWillSing(), result.getWhoWillSing());
    }

    @Test
    void testTgUserUpdate() {
        comparingEntityService = new ComparingEntityService();
        TgUser existingUser = new TgUser();
        TgUser newUser = new TgUser();

        newUser.setUserState(UserState.AWAITING_FOR_TEXT);
        newUser.setId(1234L);
        newUser.setCurrentOrderId(23L);
        newUser.setCurrentOrderId(311L);

        TgUser result = comparingEntityService.tgUserUpdate(newUser, existingUser);

        assertEquals(newUser.getOrderList(), result.getOrderList());
        assertEquals(newUser.getCurrentOrderId(), result.getCurrentOrderId());
        assertEquals(newUser.getCurrentSongId(), result.getCurrentSongId());
        assertEquals(newUser.getUserState(), result.getUserState());
    }
}

