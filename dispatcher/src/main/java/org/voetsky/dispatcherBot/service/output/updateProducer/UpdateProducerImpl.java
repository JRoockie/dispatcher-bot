package org.voetsky.dispatcherBot.service.output.updateProducer;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;


@Log4j
@Service
public class UpdateProducerImpl implements UpdateProducer {
    private final RabbitTemplate rabbitTemplate;

    public UpdateProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String rabbitQueue, Update update) {
//        if (update.hasCallbackQuery()) {
//            if (log.isDebugEnabled()) {
//                log.debug(update.getCallbackQuery().getFrom().getId());
//            }
//        } else {
//            if (log.isDebugEnabled()) {
//                log.debug(update.getMessage().getText());
//            }
//        }
        rabbitTemplate.convertAndSend(rabbitQueue, update);
    }
}
