package com.manoa.notification_service.events;

import com.manoa.notification_service.domain.NotificationService;
import com.manoa.notification_service.domain.OrderEventEntity;
import com.manoa.notification_service.domain.OrderEventRepository;
import com.manoa.notification_service.domain.model.OrderCancelledEvent;
import com.manoa.notification_service.domain.model.OrderCreatedEvent;
import com.manoa.notification_service.domain.model.OrderDeliveredEvent;
import com.manoa.notification_service.domain.model.OrderErrorEvent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class OrderEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(OrderEventHandler.class);
    private final NotificationService notificationService;
    private final OrderEventRepository eventRepository;

    OrderEventHandler(NotificationService notificationService, OrderEventRepository eventRepository) {
        this.notificationService = notificationService;
        this.eventRepository = eventRepository;
    }

    @RabbitListener(queues = "${notification.new-orders-queue}")
    public void handle(OrderCreatedEvent event) {
        if (eventRepository.existsByEventId(event.eventId())) {
            LOG.warn("Received duplicate OrderCreatedEvent with eventId: {}", event.eventId());
            return;
        }
        LOG.info("Received a OrderCreatedEvent with orderNumber:{}: ", event.orderNumber());
        notificationService.sendOrderCreatedNotification(event);
        var orderEvent = new OrderEventEntity(event.eventId());
        eventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notification.delivered-orders-queue}")
    public void handle(OrderDeliveredEvent event) {
        if (eventRepository.existsByEventId(event.eventId())) {
            LOG.warn("Received duplicate OrderDeliveredEvent with eventId: {}", event.eventId());
            return;
        }
        LOG.info("Received a OrderDeliveredEvent with orderNumber:{}: ", event.orderNumber());
        notificationService.sendOrderDeliveredNotification(event);
        var orderEvent = new OrderEventEntity(event.eventId());
        eventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notification.cancelled-orders-queue}")
    public void handle(OrderCancelledEvent event) {
        if (eventRepository.existsByEventId(event.eventId())) {
            LOG.warn("Received duplicate OrderCancelledEvent with eventId: {}", event.eventId());
            return;
        }
        notificationService.sendOrderCancelledNotification(event);
        LOG.info("Received a OrderCancelledEvent with orderNumber:{}: ", event.orderNumber());
        var orderEvent = new OrderEventEntity(event.eventId());
        eventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notification.error-orders-queue}")
    public void handle(OrderErrorEvent event) {
        if (eventRepository.existsByEventId(event.eventId())) {
            LOG.warn("Received duplicate OrderErrorEvent with eventId: {}", event.eventId());
            return;
        }
        LOG.info("Received a OrderErrorEvent with orderNumber:{}: ", event.orderNumber());
        notificationService.sendOrderErrorEventNotification(event);
        OrderEventEntity orderEvent = new OrderEventEntity(event.eventId());
        eventRepository.save(orderEvent);
    }
}
