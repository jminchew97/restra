CREATE TABLE restaurants
(
    id         UUID PRIMARY KEY,
    name       varchar,
    address    varchar,
    food_type  varchar,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE menus
(
    id         UUID PRIMARY KEY,
    name       varchar,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE item_type AS ENUM ('DESSERT', 'DRINK', 'ENTREE', 'APPETIZER');
CREATE TABLE items
(
    id          UUID PRIMARY KEY,
    name        varchar not null,
    price       integer,
    description varchar,
    item_type   item_type,
    created_at  timestamptz default current_timestamp
);

--Create and assign foreign keys to menu and item table
ALTER TABLE menus
    ADD COLUMN restaurant_id UUID;

ALTER TABLE menus
    ADD CONSTRAINT menu_constraint_restaurant_id
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurants (id);

ALTER TABLE items
    ADD COLUMN menu_id UUID;

ALTER TABLE items
    ADD COLUMN restaurant_id UUID;

ALTER TABLE items
    ADD CONSTRAINT item_constraint_menu_id
        FOREIGN KEY (menu_id)
            REFERENCES menus (id);

