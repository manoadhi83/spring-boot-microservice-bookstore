CREATE TABLE order_events (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_number VARCHAR(100) NOT NULL,
    event_id VARCHAR(100) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    payload JSON NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uniq_event_id (event_id),

    CONSTRAINT fk_order_events_order_number
        FOREIGN KEY (order_number) REFERENCES orders (order_number)
);