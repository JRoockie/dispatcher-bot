package org.voetsky.dispatcherBot.services.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.services.ConsumerService;

import static org.voetsky.model.RabbitQueue.*;

@Service
@Log4j
public class ConsumerServiceImpl implements ConsumerService {
    private final MainServiceImpl mainServiceImpl;

    public ConsumerServiceImpl(MainServiceImpl mainServiceImpl) {
        this.mainServiceImpl = mainServiceImpl;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(Update update) {
        mainServiceImpl.processTextMessage(update);
    }

    @Override
    @RabbitListener(queues = AUDIO_MESSAGE_UPDATE)
    public void consumeAudioMessageUpdates(Update update) {
        mainServiceImpl.consumeAudioMessageUpdates(update);
//        log.debug("NODE: Doc message is received");
//        var message = update.getMessage();
//        var sendMessage = new SendMessage();
//        sendMessage.setChatId(message.getChatId().toString());
//        sendMessage.setText("Doc message is received from NODE");
//        producerService.producerAnswer(sendMessage);

    }

    @Override
    @RabbitListener(queues = VOICE_MESSAGE_UPDATE)
    public void consumeVoiceMessageUpdates(Update update) {
        mainServiceImpl.consumeVoiceMessageUpdates(update);
//        log.debug("NODE: Voice message is received");
//        var message = update.getMessage();
//        var sendMessage = new SendMessage();
//        sendMessage.setChatId(message.getChatId().toString());
//        sendMessage.setText("Voice message is received from NODE");
//        producerService.producerAnswer(sendMessage);

    }

    @Override
    @RabbitListener(queues = BUTTON_UPDATE)
    public void consumeButtonUpdates(Update update) {
        mainServiceImpl.consumeButtonUpdates(update);
//        log.debug("NODE: Button message is received");
//        var sendMessage = new SendMessage();
//        sendMessage.setChatId(update.getCallbackQuery().getId());
//        sendMessage.setText("Button click is received from NODE");
//        producerService.producerAnswer(controller.updateReceiver(update));
    }

}
