SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO orders (
    id, order_number, username,
    customer_name, customer_email, customer_phone,
    delivery_address_line1, delivery_address_line2, delivery_address_city,
    delivery_address_state, delivery_address_zip_code, delivery_address_country,
    status, comments
) VALUES
(1, 'order-123', 'user', 'Adhi', 'abc@gmail.com', '11111111', '123 Main St', 'Apt 1', 'Dallas', 'TX', '75001', 'USA', 'NEW', NULL),
(2, 'order-456', 'user', 'Manohar', 'def@gmail.com', '2222222', '456 Main St', 'Apt 2', 'Hyderabad', 'TS', '500072', 'India', 'NEW', NULL);

INSERT INTO order_items (order_id, code, name, price, quantity) VALUES
(1, 'P100', 'The Hunger Games', 34.0, 2),
(1, 'P101', 'To Kill a Mockingbird', 45.40, 1),
(2, 'P102', 'The Chronicles of Narnia', 44.50, 1);
;