--Create and assign foreign keys to menu and item table
ALTER TABLE menu
    ADD COLUMN restaurant_id SERIAL;

ALTER TABLE menu
    ADD CONSTRAINT menu_constraint_restaurant_id
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurant (id);

ALTER TABLE item
    ADD COLUMN menu_id SERIAL;

ALTER TABLE item
    ADD CONSTRAINT item_constraint_menu_id
        FOREIGN KEY (menu_id)
            REFERENCES menu (id);
