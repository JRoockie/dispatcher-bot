package org.voetsky.dispatcherBot.service;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.service.interf.UpdateProducer;

@Service
@Log4j
public class UpdateProducerImpl implements UpdateProducer {
    private final RabbitTemplate rabbitTemplate;

    public UpdateProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String rabbitQueue, Update update) {
        if (update.hasCallbackQuery()){
            log.debug(update.getCallbackQuery().getFrom().getId());
        } else {
            log.debug(update.getMessage().getText());
        }
        rabbitTemplate.convertAndSend(rabbitQueue, update);
    }
}