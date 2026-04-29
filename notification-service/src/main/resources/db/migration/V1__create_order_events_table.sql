CREATE TABLE order_events (
    id BIGINT NOT NULL AUTO_INCREMENT,
    event_id TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL,
    PRIMARY KEY (id),
    UNIQUE KEY unique_event_id (event_id(255))
);