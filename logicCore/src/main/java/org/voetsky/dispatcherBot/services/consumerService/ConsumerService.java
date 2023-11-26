package org.voetsky.dispatcherBot.services.consumerService;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {
    void consumeTextMessageUpdates(Update update);
    void consumeAudioMessageUpdates(Update update);
    void consumeVoiceMessageUpdates(Update update);
    void consumeButtonUpdates(Update update);
}