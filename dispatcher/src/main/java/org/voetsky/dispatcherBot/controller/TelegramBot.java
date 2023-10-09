package org.voetsky.dispatcherBot.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.name}")
    private String botName;

    UpdateController updateController;

    public TelegramBot(UpdateController updateController) {
        this.updateController = updateController;
    }

    @PostConstruct
    public void init(){
        updateController.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        var originalMessage = update.getMessage();
        log.debug(originalMessage.getText());

        var response = new SendMessage();
        response.setChatId(originalMessage.getChatId().toString());
        response.setText("Hello from bot");
        sendAnswerMessage(response);

    }

    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                log.debug(message.getText());
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }
}
