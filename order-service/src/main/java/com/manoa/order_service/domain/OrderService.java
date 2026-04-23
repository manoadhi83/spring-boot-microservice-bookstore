package com.manoa.order_service.domain;

import com.manoa.order_service.domain.model.*;
import jakarta.transaction.Transactional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);
    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("INDIA", "USA", "GERMANY", "UK");

    private final OrderRepository repository;
    private final OrderValidator validator;
    private final OrderEventService orderEventService;

    OrderService(OrderRepository repository, OrderValidator validator, OrderEventService orderEventService) {
        this.repository = repository;
        this.validator = validator;
        this.orderEventService = orderEventService;
    }

    public CreateOrderResponse createOrder(String userName, CreateOrderRequest request) {
        validator.validate(request);
        OrderEntity order = OrderMapper.convertToEntity(request);
        order.setUserName(userName);

        OrderEntity saveOrder = repository.save(order);
        LOG.info("Created Order with orderNumber={}", saveOrder.getOrderNumber());

        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(saveOrder);
        orderEventService.save(orderCreatedEvent);

        return new CreateOrderResponse(saveOrder.getOrderNumber());
    }

    public void processOrders() {
        List<OrderEntity> orderEntityList = repository.findByStatus(OrderStatus.NEW);
        for (OrderEntity entity : orderEntityList) {
            try {
                if (canBeDelivered(entity)) {
                    LOG.info("OrderNumber: {} can be delivered", entity.getOrderNumber());
                    OrderDeliveredEvent deliveredEvent = OrderEventMapper.buildOrderDeliveredEvent(entity);
                    orderEventService.save(deliveredEvent);
                } else {
                    LOG.info("OrderNumber: {} is cancelled", entity.getOrderNumber());
                    OrderCancelledEvent cancelledEvent =
                            OrderEventMapper.buildOrderCancelledEvent(entity, "Can't deliver to the location");
                    orderEventService.save(cancelledEvent);
                }
            } catch (Exception e) {
                LOG.error("Failed to process Order with orderNumber: {}", entity.getOrderNumber(), e);
                repository.updateOrderStatus(entity.getOrderNumber(), OrderStatus.ERROR);
                orderEventService.save(OrderEventMapper.buildOrderErrorEvent(entity, e.getMessage()));
            }
        }
    }

    boolean canBeDelivered(OrderEntity entity) {
        return DELIVERY_ALLOWED_COUNTRIES.contains(entity.getAddress().country().toUpperCase());
    }
}
