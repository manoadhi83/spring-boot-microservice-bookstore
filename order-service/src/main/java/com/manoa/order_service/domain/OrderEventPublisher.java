package com.manoa.order_service.domain;

import com.manoa.order_service.ApplicationProperties;
import com.manoa.order_service.domain.model.OrderCancelledEvent;
import com.manoa.order_service.domain.model.OrderCreatedEvent;
import com.manoa.order_service.domain.model.OrderDeliveredEvent;
import com.manoa.order_service.domain.model.OrderErrorEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;

    OrderEventPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    public void publish(OrderCreatedEvent event) {
        this.send("new-oder-key", event);
    }

    public void publish(OrderDeliveredEvent event) {
        this.send("delievered-order-key", event);
    }

    public void publish(OrderCancelledEvent event) {
        this.send("cancelled-order-key", event);
    }

    public void publish(OrderErrorEvent event) {
        this.send("error-order-key", event);
    }

    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, payload);
    }
}
