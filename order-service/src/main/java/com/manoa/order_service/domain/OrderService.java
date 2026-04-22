package com.manoa.order_service.domain;

import com.manoa.order_service.domain.model.CreateOrderRequest;
import com.manoa.order_service.domain.model.CreateOrderResponse;
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

    OrderService(OrderRepository repository, OrderValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public CreateOrderResponse createOrder(String userName, CreateOrderRequest request) {
        validator.validate(request);
        OrderEntity order = OrderMapper.convertToEntity(request);
        order.setUserName(userName);

        OrderEntity saveOrder = repository.save(order);
        LOG.info("Created Order with orderNumber={}", saveOrder.getOrderNumber());
        return new CreateOrderResponse(saveOrder.getOrderNumber());
    }
}
