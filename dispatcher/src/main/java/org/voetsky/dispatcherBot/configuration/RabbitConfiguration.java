package org.voetsky.dispatcherBot.configuration;

import lombok.Getter;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class RabbitConfiguration {

    @Value("${spring.rabbitmq.queues.text}")
    private String textMessageUpdateQueue;

    @Value("${spring.rabbitmq.queues.audio}")
    private String audioMessageUpdateQueue;

    @Value("${spring.rabbitmq.queues.voice}")
    private String voiceMessageUpdateQueue;

    @Value("${spring.rabbitmq.queues.button}")
    private String buttonMessageQueue;

    @Value("${spring.rabbitmq.queues.answer}")
    private String answerMessageQueue;

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue textMessageQueue() {
        return new Queue(textMessageUpdateQueue);
    }

    @Bean
    public Queue mp3MessageQueue() {
        return new Queue(audioMessageUpdateQueue);
    }

    @Bean
    public Queue voiceMessageQueue() {
        return new Queue(voiceMessageUpdateQueue);
    }
    @Bean
    public Queue buttonQueue() {
        return new Queue(buttonMessageQueue);
    }

    @Bean
    public Queue answerMessageQueue() {
        return new Queue(answerMessageQueue);
    }


}
