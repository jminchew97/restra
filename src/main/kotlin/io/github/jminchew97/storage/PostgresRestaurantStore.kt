package io.github.jminchew97.storage

import io.github.jminchew97.HikariService
import io.github.jminchew97.models.*
import io.github.jminchew97.toUuid
import kotlinx.datetime.Clock
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlinx.datetime.Instant

class PostgresRestaurantStore(private val hs: HikariService) : RestaurantStore {

    override fun getRestaurants(): Collection<Restaurant> {
        val resultList = mutableListOf<Restaurant>()
        hs.withConnection { connection ->
            val sqlStatement: String = "SELECT * FROM restaurants;"
            val prp: PreparedStatement = connection.prepareStatement(sqlStatement)

            val rs: ResultSet = prp.executeQuery()

            while (rs.next()) {
                val rr: Restaurant = Restaurant(
                    RestaurantId(rs.getString("id")),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("food_type"),
                    rs.getString("created_at")
                )
                resultList.add(rr)
            }
            resultList
        }
        return resultList
    }

    override fun getRestaurant(id: RestaurantId): Restaurant? {
        val rr: Restaurant? = hs.withConnection { connection ->

            val sqlStatement: String = "SELECT * FROM restaurants WHERE id=?;"
            val prp: PreparedStatement = connection.prepareStatement(sqlStatement)
            prp.setString(1, id.unwrap.toString())
            val rs: ResultSet = prp.executeQuery()

            if (!rs.next()) null else

                Restaurant(
                    RestaurantId(rs.getString("id")),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("food_type"),
                    rs.getString("created_at")
                )
        }
        return rr
    }

    override fun createRestaurant(restaurant: CreateRestaurant): Boolean {
//        INSERT INTO restaurant (name, address,foodType)
//        VALUES ('Burger Palace', '123 westore ave', 'american');
        val resultInt = hs.withConnection { connection ->

            val prp: PreparedStatement =
                connection.prepareStatement("INSERT INTO restaurants (name, address,food_type) VALUES (?,?,?);")
            prp.setString(1, restaurant.name)
            prp.setString(2, restaurant.address)
            prp.setString(3, restaurant.foodType)
            prp.executeUpdate()
        }
        return resultInt == 1
    }

    override fun deleteRestaurant(id: RestaurantId): Boolean {
        val resultInt = hs.withConnection { connection ->
            val prp: PreparedStatement = connection.prepareStatement("DELETE FROM restaurants WHERE id=?;")
            prp.setString(1, id.unwrap.toString())
            prp.executeUpdate()

        }
        return resultInt == 1
    }

    override fun updateRestaurant(updateRest: UpdateRestaurant): Boolean {
        val result: Int = hs.withConnection { connection ->
            val sqlString = "UPDATE restaurants SET name = ?, address = ?, food_type = ? WHERE id = ?"

            val prep: PreparedStatement = connection.prepareStatement(sqlString)

            prep.setString(1, updateRest.name)
            prep.setString(2, updateRest.address)
            prep.setString(3, updateRest.foodType)
            prep.setString(4, updateRest.id.unwrap.toString())
            prep.executeUpdate()
        }
        return result == 1
    }

    override fun getMenusFromRestaurant(restId: RestaurantId): Collection<Menu> {
        val resultList: MutableList<Menu> = hs.withConnection { connection ->
            val sql: String = "SELECT * FROM menus WHERE restaurant_id=?;"
            val prp = connection.prepareStatement(sql)
            prp.setString(1, restId.unwrap.toString())
            val rs = prp.executeQuery()

            val menuList = mutableListOf<Menu>()
            while (rs.next()) {
                menuList.add(
                    Menu(
                        MenuId(rs.getInt("id").toString()),
                        RestaurantId(rs.getInt("restaurant_id").toString()),
                        rs.getString("name"),
                        rs.getString("created_at")
                    )
                )
            }
            menuList
        }
        return resultList
    }

    override fun getMenu(menuId: MenuId): Menu? {
        val menu: Menu? = hs.withConnection { connection ->
            val sql = "SELECT * FROM menus WHERE id=?;"
            val prp = connection.prepareStatement(sql)

            prp.setString(1, menuId.unwrap.toString())
            val rs: ResultSet = prp.executeQuery()

            if (!rs.next()) null else

                Menu(
                    MenuId(rs.getInt("id").toString()),
                    RestaurantId(rs.getInt("restaurant_id").toString()),
                    rs.getString("name"),
                    rs.getString("created_at")
                )
        }
        return menu
    }

    override fun createMenu(menu: CreateMenu): Boolean {

        val result = hs.withConnection { connection ->


            val sql = "INSERT INTO menus (restaurant_id, name) VALUES (?,?);"
            val prp = connection.prepareStatement(sql)

            prp.setString(1, menu.restaurantId.unwrap.toString())
            prp.setString(2, menu.name)
            prp.executeUpdate()
        }
        return result == 1
    }

    override fun deleteMenu(restId: RestaurantId, menuId: MenuId): Boolean {
        val result = hs.withConnection { connection ->
            val prp: PreparedStatement =
                connection.prepareStatement("DELETE FROM menus WHERE restaurant_id=? AND id=?;")

            prp.setString(1, restId.unwrap.toString())
            prp.setString(2, menuId.unwrap.toString())
            prp.executeUpdate()
        }
        return result == 1
    }

    override fun getAllMenus(): Collection<Menu> {
        val resultList: MutableList<Menu> = hs.withConnection { connection ->
            val sql: String = "SELECT * FROM menus;"
            val prp = connection.prepareStatement(sql)
            val rs = prp.executeQuery()

            val menuList = mutableListOf<Menu>()
            while (rs.next()) {
                menuList.add(
                    Menu(
                        MenuId(rs.getString("id").toUuid()),
                        RestaurantId(rs.getString("restaurant_id").toString()),
                        rs.getString("name"),
                        rs.getString("created_at")
                    )
                )
            }
            menuList
        }
        return resultList
    }

    override fun updateMenu(newMenu: UpdateMenu): Boolean {
        val result = hs.withConnection { connection ->
            val sql = "UPDATE menus SET name = ? WHERE id = ? AND restaurant_id = ?"
            val prp = connection.prepareStatement(sql)

            prp.setString(1, newMenu.name)
            prp.setString(2, newMenu.menuId.unwrap.toString())
            prp.setString(3, newMenu.restaurantId.unwrap.toString())
            prp.executeUpdate()
        }
        return result == 1
    }

    override fun createItem(createItem: CreateItem): Boolean {
        val result = hs.withConnection { connection ->
            val sql = "INSERT INTO items (menu_id, name, price, description, item_type, restaurant_id) VALUES (?, ?, ?, ?, ?::item_type, ?);"
            val prp = connection.prepareStatement(sql)

            prp.setString(1, createItem.menuId.unwrap.toString())
            prp.setString(2, createItem.name)
            prp.setInt(3, createItem.price.unwrap)
            prp.setString(4, createItem.description)
            prp.setString(5, createItem.itemType.uppercase())
            prp.setString(6, createItem.restaurantId.unwrap.toString())
            prp.executeUpdate()
        }
        return result == 1
    }

    override fun getItem(itemId: ItemId): Item? {
        val item = hs.withConnection { connection ->
            val sql = "select * from items where id=?::UUID"
            val prp = connection.prepareStatement(sql)

            prp.setString(1, itemId.unwrap.toString())
            val rs: ResultSet = prp.executeQuery()
            rs.next()
            Item(
                ItemId(rs.getString("id").toUuid()),
                MenuId(rs.getString("menu_id").toUuid()),
                RestaurantId(rs.getString("restaurant_id").toUuid()),
                rs.getString("name"),
                rs.getString("description"),
                Cents(rs.getInt("price")),
                ItemType.valueOf(rs.getString("item_type").uppercase()),
                Instant.parse(rs.getString("created_at").replace(" ", "T"))
            )
        }
        return item
    }


    override fun deleteItem(itemId: ItemId, restId: RestaurantId, menuId: MenuId): Boolean {
        val result = hs.withConnection { connection ->
            val prp: PreparedStatement =
                connection.prepareStatement("DELETE FROM items WHERE id=?::UUID and menu_id=?::UUID and restaurant_id=?::UUID;")

            prp.setString(1, itemId.unwrap.toString())
            prp.setString(2, menuId.unwrap.toString())
            prp.setString(3, restId.unwrap.toString())

            prp.executeUpdate()
        }
        return result == 1
    }

    override fun getAllItems(): Collection<Item> {
        val resultList: MutableList<Item> = hs.withConnection { connection ->
            val sql = "SELECT * FROM items;"
            val prp = connection.prepareStatement(sql)
            val rs = prp.executeQuery()

            val itemList = mutableListOf<Item>()
            while (rs.next()) {
                itemList.add(
                    Item(
                        ItemId(rs.getString("id").toUuid()),
                        MenuId(rs.getString("menu_id").toUuid()),
                        RestaurantId(rs.getString("restaurant_id").toUuid()),
                        rs.getString("name"),
                        rs.getString("description"),
                        Cents(rs.getInt("price")),
                        ItemType.valueOf(rs.getString("item_type").uppercase()),
                        Instant.parse(rs.getString("created_at").replace(" ", "T"))
                    )
                )
            }
            itemList
        }
        return resultList
    }

    override fun updateItem(updateItem: UpdateItem): Boolean {
        val result = hs.withConnection { connection ->
            val prp: PreparedStatement =
                connection.prepareStatement(
                    """
                    UPDATE items
                    SET menu_id=?::UUID, restaurant_id=?::UUID, name=?, description=?, price=?, item_type=?::item_type
                    WHERE id=?::UUID
                    """
                )
            prp.setString(1, updateItem.menuId.unwrap.toString())
            prp.setString(2, updateItem.restaurantId.unwrap.toString())
            prp.setString(3, updateItem.name)
            prp.setString(4, updateItem.description)
            prp.setInt(5, updateItem.price.unwrap)
            prp.setString(6, updateItem.itemType)
            prp.setString(7,updateItem.itemId.unwrap.toString())


            prp.executeUpdate() == 1
        }
        return result
    }

    override fun getItemsByMenu(menuId: MenuId): Collection<Item> {
        val result = hs.withConnection {
            connection ->
            val sql = "select * from items where menu_id=?::UUID"
            val prp = connection.prepareStatement(sql)

            prp.setString(1,menuId.unwrap.toString())
            val rs = prp.executeQuery()

            val itemList = mutableListOf<Item>()
            while (rs.next()) {
                itemList.add(
                    Item(
                        ItemId(rs.getString("id").toUuid()),
                        MenuId(rs.getString("menu_id").toUuid()),
                        RestaurantId(rs.getString("restaurant_id").toUuid()),
                        rs.getString("name"),
                        rs.getString("description"),
                        Cents(rs.getInt("price")),
                        ItemType.valueOf(rs.getString("item_type").uppercase()),
                        Instant.parse(rs.getString("created_at").replace(" ", "T"))
                    )
                )
            }
            itemList
        }
        return result
    }

    override fun getItemsByRestaurant(restaurantId: RestaurantId): Collection<Item> {
        val result = hs.withConnection {
                connection ->
            val sql = "select * from items where restaurant_id=?::UUID"
            val prp = connection.prepareStatement(sql)

            prp.setString(1,restaurantId.unwrap.toString())
            val rs = prp.executeQuery()

            val itemList = mutableListOf<Item>()
            while (rs.next()) {
                itemList.add(
                    Item(
                        ItemId(rs.getString("id").toUuid()),
                        MenuId(rs.getString("menu_id").toUuid()),
                        RestaurantId(rs.getString("restaurant_id").toUuid()),
                        rs.getString("name"),
                        rs.getString("description"),
                        Cents(rs.getInt("price")),
                        ItemType.valueOf(rs.getString("item_type").uppercase()),
                        Instant.parse(rs.getString("created_at").replace(" ", "T"))
                    )
                )
            }
            itemList
        }
        return result
    }
}
