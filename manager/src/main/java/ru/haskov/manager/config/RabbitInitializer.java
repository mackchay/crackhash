package ru.haskov.manager.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RabbitInitializer {

    private final RabbitAdmin rabbitAdmin;
    private final Queue taskRequestQueue;
    private final Queue taskResponseQueue;
    private final Queue dlqQueue;
    private final DirectExchange tasksExchange;
    private final DirectExchange dlxExchange;
    private final Binding taskRequestBinding;
    private final Binding taskResponseBinding;
    private final Binding dlqBinding;

    @PostConstruct
    private void declareRabbitComponents() {
        rabbitAdmin.declareQueue(taskRequestQueue);
        rabbitAdmin.declareQueue(taskResponseQueue);
        rabbitAdmin.declareQueue(dlqQueue);
        rabbitAdmin.declareExchange(tasksExchange);
        rabbitAdmin.declareExchange(dlxExchange);
        rabbitAdmin.declareBinding(taskRequestBinding);
        rabbitAdmin.declareBinding(taskResponseBinding);
        rabbitAdmin.declareBinding(dlqBinding);
    }
}
