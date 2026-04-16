CREATE TABLE products (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    image_url TEXT,
    price DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id)
);