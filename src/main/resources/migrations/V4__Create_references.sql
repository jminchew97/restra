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
    ADD CONSTRAINT item_constraint_menu_id
        FOREIGN KEY (menu_id)
            REFERENCES menus (id);
