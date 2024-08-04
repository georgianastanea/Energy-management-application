package utcn.monitoringservice.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfiguration {

    public static final String MEASUREMENTS_QUEUE = "measurements_queue";
    public static final String DEVICE_QUEUE = "device_queue";
    public static final String EXCHANGE = "message_exchange";
    public static final String MEASUREMENTS_ROUTING_KEY = "measurements_routingKey";
    public static final String DEVICE_ROUTING_KEY = "device_routingKey";

    @Bean
    @Qualifier("measurementsQueue")
    public Queue measurementsQueue() {
        return new Queue(MEASUREMENTS_QUEUE);
    }

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
    public Binding measurementsBinding(@Qualifier("measurementsQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(MEASUREMENTS_ROUTING_KEY);
    }

    @Bean
    public Binding deviceBinding(@Qualifier("deviceQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(DEVICE_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleMessageListenerContainer measurementsContainer(ConnectionFactory connectionFactory, MessageListenerAdapter measurementsListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(measurementsListenerAdapter);
        container.setQueueNames(MEASUREMENTS_QUEUE);
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer deviceContainer(ConnectionFactory connectionFactory, MessageListenerAdapter deviceListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(deviceListenerAdapter);
        container.setQueueNames(DEVICE_QUEUE);
        return container;
    }

    @Bean
    public MessageListenerAdapter measurementsListenerAdapter(MeasurementsListener measurementsListener) {
        return new MessageListenerAdapter(measurementsListener, "onMeasurementsMessageReceived");
    }

    @Bean
    public MessageListenerAdapter deviceListenerAdapter(DeviceListener deviceListener) {
        return new MessageListenerAdapter(deviceListener, "onDeviceReceived");
    }
}
