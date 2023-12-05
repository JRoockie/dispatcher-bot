package org.voetsky.dispatcherBot.services.input.consumerService;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.services.input.ReceiverController;

import static org.voetsky.model.RabbitQueue.*;


@Log4j
@Service
public class ConsumerServiceImpl implements ConsumerService {
    private final ReceiverController receiverController;

    public ConsumerServiceImpl(ReceiverController receiverController) {
        this.receiverController = receiverController;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(Update update) {
        receiverController.processTextMessage(update);
    }

    @Override
    @RabbitListener(queues = AUDIO_MESSAGE_UPDATE)
    public void consumeAudioMessageUpdates(Update update) {
        receiverController.consumeAudioMessageUpdates(update);
    }

    @Override
    @RabbitListener(queues = VOICE_MESSAGE_UPDATE)
    public void consumeVoiceMessageUpdates(Update update) {
        receiverController.consumeVoiceMessageUpdates(update);
    }

    @Override
    @RabbitListener(queues = BUTTON_UPDATE)
    public void consumeButtonUpdates(Update update) {
        receiverController.consumeButtonUpdates(update);
    }

}
