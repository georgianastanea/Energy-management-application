package ro.tuc.ds2023.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfiguration {

    public static final String DEVICE_QUEUE = "device_queue";
    public static final String EXCHANGE = "message_exchange";
    public static final String DEVICE_ROUTING_KEY = "device_routingKey";

    @Bean
    @Qualifier("deviceQueue")
    public Queue deviceQueue() {
        return new Queue(DEVICE_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding deviceBinding(@Qualifier("deviceQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(DEVICE_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}