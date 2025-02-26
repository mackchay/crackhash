package ru.haskov.worker.config;


import lombok.Setter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
public class RabbitConfiguration {

    @Value("${queue.task_request}")
    private String tasksRequestQueue;

    @Value("${queue.task_response}")
    private String tasksResponseQueue;

    @Value("${queue.dlq}")
    private String dlqQueue;

    @Value("${exchange.tasks}")
    private String tasksExchange;

    @Value("${exchange.dlx}")
    private String dlxExchange;

    @Value("${routing.task_request}")
    private String routingRequestKey;

    @Value("${routing.task_response}")
    private String routingResponseKey;

    @Value("${routing.dlq}")
    private String dlqRoutingKey;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.port}")
    private Integer port;

    @Bean
    public Queue taskRequestQueue() {
        return QueueBuilder.durable(tasksRequestQueue)
                .deadLetterExchange(dlxExchange)
                .deadLetterRoutingKey(dlqRoutingKey)
                .build();
    }

    @Bean
    public Queue taskResponseQueue() {
        return QueueBuilder.durable(tasksResponseQueue)
                .deadLetterExchange(dlxExchange)
                .deadLetterRoutingKey(dlqRoutingKey)
                .build();
    }

    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(dlqQueue).build();
    }

    @Bean
    public DirectExchange tasksExchange() {
        return new DirectExchange(tasksExchange);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(dlxExchange);
    }

    @Bean
    public Binding taskRequestBinding(DirectExchange tasksExchange) {
        return BindingBuilder.bind(taskRequestQueue()).to(tasksExchange).with(routingRequestKey);
    }

    @Bean
    public Binding taskResponseBinding(DirectExchange tasksExchange) {
        return BindingBuilder.bind(taskResponseQueue()).to(tasksExchange).with(routingResponseKey);
    }

    @Bean
    public Binding dlqBinding(Queue dlqQueue, DirectExchange dlxExchange) {
        return BindingBuilder.bind(dlqQueue).to(dlxExchange).with(dlqRoutingKey);
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter()); // Устанавливаем JSON-конвертер
        return rabbitTemplate;
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            CachingConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(1);  // Количество воркеров
        factory.setPrefetchCount(1);  // 1 задача на воркера
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}
