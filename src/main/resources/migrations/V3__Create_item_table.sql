CREATE TYPE item_type AS ENUM ('dessert', 'drink', 'entree', 'appetizer');
CREATE TABLE items
(
    id          SERIAL PRIMARY KEY,
    name        varchar,
    price       numeric,
    description varchar,
    item_type   item_type,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
