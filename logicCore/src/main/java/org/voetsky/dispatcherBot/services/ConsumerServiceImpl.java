package org.voetsky.dispatcherBot.services;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.services.interf.ConsumerService;

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
    }

    @Override
    @RabbitListener(queues = VOICE_MESSAGE_UPDATE)
    public void consumeVoiceMessageUpdates(Update update) {
        mainServiceImpl.consumeVoiceMessageUpdates(update);
    }

    @Override
    @RabbitListener(queues = BUTTON_UPDATE)
    public void consumeButtonUpdates(Update update) {
        mainServiceImpl.consumeButtonUpdates(update);
    }

}
