package org.voetsky.dispatcherBot.services.input.consumerService;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.services.input.ReceiverController;


@Log4j
@Service
public class ConsumerService implements Consumer {

    private final ReceiverController receiverController;

    public ConsumerService(ReceiverController receiverController) {
        this.receiverController = receiverController;
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.text}")
    public void consumeTextMessageUpdates(Update update) {
        receiverController.processTextMessage(update);
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.audio}")
    public void consumeAudioMessageUpdates(Update update) {
        receiverController.consumeAudioMessageUpdates(update);
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.voice}")
    public void consumeVoiceMessageUpdates(Update update) {
        receiverController.consumeVoiceMessageUpdates(update);
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.button}")
    public void consumeButtonUpdates(Update update) {
        receiverController.consumeButtonUpdates(update);
    }

}
