package org.voetsky.dispatcherBot.service;

import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProducer {
    void produce(String rabbitQueue, Update update);
}