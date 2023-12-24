package org.voetsky.dispatcherBot.controller;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.service.messageutils.MakeMessage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateControllerTest extends TestCase {

    @InjectMocks
    private UpdateController updateController;

    @Mock
    private Logger log;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private MakeMessage makeMessage;

    @Test
    public void testRegisterBot() {
        assertThat(updateController).isNotNull();
    }

    @Test(expected = NotAMockException.class)
    public void testProcessUpdate() {
        Update update = new Update();

        updateController.processUpdate(update);
        verify(updateController).distributeMessagesByType(update);

    }

    @Test(expected = NotAMockException.class)
    public void testDistributeMessagesByType() {
        Update update = new Update();

        updateController.processUpdate(update);

        verify(updateController).setUnsupportedMessageTypeView(update);
        verify(log).error(Mockito.anyString(), Mockito.any(Object[].class));
    }

    @Test(expected = NotAMockException.class)
    public void testSetUnsupportedMessageTypeView() {
        Update update = new Update();

        updateController.setUnsupportedMessageTypeView(update);

        verify(updateController).setUnsupportedMessageTypeView(update);
        verify(makeMessage).generateSendMessageWithText(update, "type.error");
    }

    @Test(expected = NotAMockException.class)
    public void testSetFileIsReceivedView() {
        Update update = new Update();

        updateController.setFileIsReceivedView(update);

        verify(makeMessage).generateSendMessageWithText(update, "process");
        verify(updateController).setView(new SendMessage());
    }

}