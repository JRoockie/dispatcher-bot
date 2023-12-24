package org.voetsky.dispatcherBot.service.messageutils;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.configuration.dispatcherLocalization.DispatcherLang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class MakeMessageTest {

    @InjectMocks
    private MakeMessage makeMessage;

    @Mock
    private DispatcherLang dispatcherLang;

    @Test
    public void testGenerateSendMessageWithText() {
        String text = "process";
        User user = new User();
        user.setId(123L);
        user.setLanguageCode("ru");
        Chat chat = new Chat();
        chat.setId(1234L);
        Message message = new Message();
        message.setFrom(user);
        message.setChat(chat);
        message.setText(text);
        Update update = new Update();
        update.setMessage(message);
        SendMessage expectedSendMessage = new SendMessage();

        String newText = "Обработка...";
        expectedSendMessage.setChatId("1234");
        expectedSendMessage.setText(newText);
        when(dispatcherLang.get("ru", text)).thenReturn(newText);
        SendMessage actualSendMessage = makeMessage.generateSendMessageWithText(update, text);

        assertEquals(expectedSendMessage, actualSendMessage);
    }

    @Test
    public void testLocalize() {
        User user = new User();
        user.setId(123L);
        user.setLanguageCode("ru");
        Message message = new Message();
        message.setFrom(user);
        message.setText("process");
        Update update = new Update();
        update.setMessage(message);
        String key = "process";
        String localizedText = "Обработка...";

        when(dispatcherLang.get("ru", key)).thenReturn(localizedText);

        String actualLocalizedText = makeMessage.localize(update, key);

        assertEquals(localizedText, actualLocalizedText);
    }
}
