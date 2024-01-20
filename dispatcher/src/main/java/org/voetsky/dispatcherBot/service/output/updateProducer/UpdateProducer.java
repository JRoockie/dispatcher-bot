package org.voetsky.dispatcherBot.service.output.updateProducer;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProducer {

    void produce(String rabbitQueue, Update update);

}