package com.manoa.notification_service.events;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manoa.notification_service.AbstractIntegrationTests;
import com.manoa.notification_service.ApplicationProperties;
import com.manoa.notification_service.domain.model.Address;
import com.manoa.notification_service.domain.model.Customer;
import com.manoa.notification_service.domain.model.OrderCreatedEvent;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderEventHandlerTests extends AbstractIntegrationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ApplicationProperties properties;

    Customer customer = new Customer("Manoa", "manoagmail.com", "999999999");
    Address address = new Address("addr line 1", null, "Puducherry", "PU", "65008", "India");

    // @Test
    void shouldHandleOrderCreatedEvent() {
        String orderNumber = UUID.randomUUID().toString();

        var event = new OrderCreatedEvent(
                UUID.randomUUID().toString(), orderNumber, Set.of(), customer, address, LocalDateTime.now());
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), properties.newOrdersQueue(), event);

        await().atMost(120, SECONDS).untilAsserted(() -> {
            verify(notificationService).sendOrderCreatedNotification(any(OrderCreatedEvent.class));
        });
    }
}
