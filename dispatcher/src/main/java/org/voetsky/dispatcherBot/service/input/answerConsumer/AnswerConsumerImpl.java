package org.voetsky.dispatcherBot.service.input.answerConsumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.voetsky.dispatcherBot.controller.UpdateController;

@Service
public class AnswerConsumerImpl implements AnswerConsumer {
    private final UpdateController updateController;

    public AnswerConsumerImpl(UpdateController updateController) {
        this.updateController = updateController;
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.answer}")
    public void consume(SendMessage sendMessage) {
        updateController.setView(sendMessage);
    }
}
