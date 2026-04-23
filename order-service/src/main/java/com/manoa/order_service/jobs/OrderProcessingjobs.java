package com.manoa.order_service.jobs;

import com.manoa.order_service.domain.OrderService;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessingjobs {

    private static final Logger LOG = LoggerFactory.getLogger(OrderProcessingjobs.class);

    private final OrderService orderService;

    OrderProcessingjobs(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "${orders.new-orders-job-cron}")
    void processOrderJobs() {
        LOG.info("Processing new orders at {}", Instant.now());
        orderService.processOrders();
    }
}
