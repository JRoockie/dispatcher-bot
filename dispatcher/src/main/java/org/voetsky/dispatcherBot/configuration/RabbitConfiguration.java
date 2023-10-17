package org.voetsky.dispatcherBot.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.voetsky.model.RabbitQueue.*;


@Configuration
public class RabbitConfiguration {
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue textMessageQueue() {
        return new Queue(TEXT_MESSAGE_UPDATE);
    }

    @Bean
    public Queue mp3MessageQueue() {
        return new Queue(AUDIO_MESSAGE_UPDATE);
    }

    @Bean
    public Queue voiceMessageQueue() {
        return new Queue(VOICE_MESSAGE_UPDATE);
    }
    @Bean
    public Queue buttonQueue() {
        return new Queue(BUTTON_UPDATE);
    }

    @Bean
    public Queue answerMessageQueue() {
        return new Queue(ANSWER_MESSAGE);
    }


}
