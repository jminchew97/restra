CREATE TABLE restaurants
(
    id         SERIAL PRIMARY KEY,
    name       varchar,
    address    varchar,
    food_type  varchar,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO restaurants (name, address,food_type)
VALUES ('Best Dim Sum', '123 da street', 'dim sum');

INSERT INTO restaurants (name, address,food_type)
VALUES ('Burger Palace', '123 westore ave', 'american');

CREATE TABLE menus
(
    id         SERIAL PRIMARY KEY,
    name       varchar,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TYPE item_type AS ENUM ('dessert', 'drink', 'entree', 'appetizer');
CREATE TABLE items
(
    id          SERIAL PRIMARY KEY,
    name        varchar,
    price       integer,
    description varchar,
    item_type   item_type,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


--Create and assign foreign keys to menu and item table
ALTER TABLE menus
    ADD COLUMN restaurant_id SERIAL;

ALTER TABLE menus
    ADD CONSTRAINT menu_constraint_restaurant_id
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurants (id);

ALTER TABLE items
    ADD COLUMN menu_id SERIAL;

ALTER TABLE items
    ADD COLUMN restaurant_id SERIAL;
ALTER TABLE items
    ADD CONSTRAINT item_constraint_menu_id
        FOREIGN KEY (menu_id)
            REFERENCES menus (id);


INSERT INTO menus (restaurant_id, name)
VALUES (1,'test');
