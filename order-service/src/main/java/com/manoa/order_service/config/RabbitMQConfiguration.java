package com.manoa.order_service.config;

import com.manoa.order_service.ApplicationProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    private final ApplicationProperties properties;

    RabbitMQConfiguration(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(properties.orderEventsExchange());
    }

    @Bean
    Queue newOrderQueue() {
        return QueueBuilder.durable(properties.newOrdersQueue()).build();
    }

    @Bean
    Binding newOrderQueueBinding() {
        return BindingBuilder.bind(newOrderQueue()).to(exchange()).with("new-oder-key");
    }

    @Bean
    Queue deliveredOrderQueue() {
        return QueueBuilder.durable(properties.deliveredOrdersQueue()).build();
    }

    @Bean
    Binding deliveredOrderBinding() {
        return BindingBuilder.bind(deliveredOrderQueue()).to(exchange()).with("delievered-order-key");
    }

    @Bean
    Queue cancelledOrderQueue() {
        return QueueBuilder.durable(properties.cancelledOrdersQueue()).build();
    }

    @Bean
    Binding cancelledOrderBinding() {
        return BindingBuilder.bind(cancelledOrderQueue()).to(exchange()).with("cancelled-order-key");
    }

    @Bean
    Queue errorOrderQueue() {
        return QueueBuilder.durable(properties.errorOrdersQueue()).build();
    }

    @Bean
    Binding errorOrderQueueBinding() {
        return BindingBuilder.bind(errorOrderQueue()).to(exchange()).with("error-order-key");
    }

    @Bean
    public JacksonJsonMessageConverter jacksonConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter());
        return rabbitTemplate;
    }

    @Bean
    CommandLineRunner testMessage(RabbitTemplate rabbitTemplate) {
        return args -> {
            System.out.println("Sending test message...");
            rabbitTemplate.convertAndSend("orders-exchange", "new-oder-key", "hello");
        };
    }
}
