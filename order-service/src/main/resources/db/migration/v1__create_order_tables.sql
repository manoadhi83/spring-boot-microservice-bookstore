CREATE TABLE orders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_number VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    customer_phone VARCHAR(50) NOT NULL,
    delivery_address_line1 VARCHAR(255) NOT NULL,
    delivery_address_line2 VARCHAR(255),
    delivery_address_city VARCHAR(100) NOT NULL,
    delivery_address_state VARCHAR(100) NOT NULL,
    delivery_address_zip_code VARCHAR(20) NOT NULL,
    delivery_address_country VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    comments TEXT,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE order_items (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    order_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_order_items_orders
        FOREIGN KEY (order_id) REFERENCES orders(id)
        ON DELETE CASCADE
);
