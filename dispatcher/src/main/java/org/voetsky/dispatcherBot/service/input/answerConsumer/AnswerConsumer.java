package org.voetsky.dispatcherBot.service.input.answerConsumer;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface AnswerConsumer {
    void consume(SendMessage sendMessage);
}