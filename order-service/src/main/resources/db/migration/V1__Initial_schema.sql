CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    product_id BIGSERIAL NOT NULL,
    product_name varchar(255),
    product_price float8,
    quantity int NOT NULL,
    status varchar(255) NOT NULL,
    created_date timestamp NOT NULL,
    last_modified_date timestamp NOT NULL,
    version integer NOT NULL
);