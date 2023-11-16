CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    total float8 NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    version INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    order_id BIGINT,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    version INTEGER NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);
