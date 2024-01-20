package org.voetsky.dispatcherBot.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Log4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.name}")
    private String botName;

    @Autowired
    private final UpdateController updateController;

    public TelegramBot(@Value("${bot.token}") String botToken,
                       @Value("${bot.name}") String botName,
                       UpdateController updateController) {
        super(botToken);
        this.botToken = botToken;
        this.botName = botName;
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
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug(message.getText());
                }
                execute(message);
            } catch (TelegramApiException e) {
                if (log.isDebugEnabled()) {
                    log.error(e.getMessage());
                }
            }
        }
    }
}
