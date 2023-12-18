package org.voetsky.dispatcherBot.services.input.consumerService;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Consumer {
    void consumeTextMessageUpdates(Update update);
    void consumeAudioMessageUpdates(Update update);
    void consumeVoiceMessageUpdates(Update update);
    void consumeButtonUpdates(Update update);
}