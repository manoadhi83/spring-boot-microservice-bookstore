package com.manoa.order_service.domain;

import com.manoa.order_service.domain.model.CreateOrderRequest;
import com.manoa.order_service.domain.model.CreateOrderResponse;
import com.manoa.order_service.domain.model.OrderCreatedEvent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);
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
}
