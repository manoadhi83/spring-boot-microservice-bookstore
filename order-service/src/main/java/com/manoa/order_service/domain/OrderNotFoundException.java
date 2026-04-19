package com.manoa.order_service.domain;

public class OrderNotFoundException extends Exception {

    public OrderNotFoundException(String message) {
        super(message);
    }

    public static OrderNotFoundException forOrderNumber(String orderNumber) {
        return new OrderNotFoundException("Order Number " + orderNumber + " not found");
    }
}
