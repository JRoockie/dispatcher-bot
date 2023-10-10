package org.voetsky.dispatcherBot.services;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {
    void consumeTextMessageUpdates(Update update);
    void consumeDocMessageUpdates(Update update);
    void consumeVoiceMessageUpdates(Update update);
}