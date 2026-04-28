package com.manoa.order_service.web.controllers;

import com.manoa.order_service.domain.OrderNotFoundException;
import com.manoa.order_service.domain.OrderService;
import com.manoa.order_service.domain.SecurityService;
import com.manoa.order_service.domain.model.CreateOrderRequest;
import com.manoa.order_service.domain.model.CreateOrderResponse;
import com.manoa.order_service.domain.model.OrderDTO;
import com.manoa.order_service.domain.model.OrderSummary;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    private final SecurityService securityService;
    private final OrderService orderService;

    OrderController(SecurityService securityService, OrderService orderService) {
        this.securityService = securityService;
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
        String user = securityService.getLoginUserName();
        return orderService.createOrder(user, orderRequest);
    }

    @GetMapping
    List<OrderSummary> getOrder() {
        String userName = securityService.getLoginUserName();
        LOG.info("Fetching orders for user: {}", userName);
        return orderService.findOrders(userName);
    }

    @GetMapping(value = "/{orderNumber}")
    OrderDTO getOrder(@PathVariable(value = "orderNumber") String orderNumber) throws OrderNotFoundException {
        LOG.info("Fetching order by id: {}", orderNumber);
        String userName = securityService.getLoginUserName();
        return orderService
                .findUserOrder(userName, orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
    }
}
