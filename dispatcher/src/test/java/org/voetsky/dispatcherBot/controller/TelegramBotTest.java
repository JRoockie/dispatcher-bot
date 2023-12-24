package org.voetsky.dispatcherBot.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class TelegramBotTest {

    @Autowired
    private TelegramBot telegramBot;

    @Test
    void getBotUsername() {
        assertThat(telegramBot.getBotUsername()).isNotNull();
    }

    @Test
    void getBotToken() {
        assertThat(telegramBot.getBotToken()).isNotNull();
    }

    @Test
    void sendAnswerMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId("2232323");
        Assertions.assertThrows(
                NullPointerException.class, () -> telegramBot.sendAnswerMessage(sendMessage));
    }


}