CREATE TABLE restaurant
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR,
    address    VARCHAR,
    food_type  varchar,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
