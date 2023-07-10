package io.github.jminchew97.storage

import io.github.jminchew97.HikariService
import io.github.jminchew97.models.*
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlinx.datetime.Instant
import kotlinx.uuid.toJavaUUID
import kotlinx.uuid.toKotlinUUID

class PostgresRestaurantStore(private val hs: HikariService) : RestaurantStore {

    override fun getRestaurants(): Collection<Restaurant> {
        val resultList = mutableListOf<Restaurant>()
        hs.withConnection { connection ->
            val sqlStatement: String = "SELECT * FROM restaurants;"
            val prp: PreparedStatement = connection.prepareStatement(sqlStatement)
            val rs: ResultSet = prp.executeQuery()
            while (rs.next()) {
                val rr: Restaurant = Restaurant(
                    RestaurantId((rs.getObject("id",java.util.UUID::class.java).toKotlinUUID())),
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
            val sqlStatement: String = "SELECT * FROM restaurants WHERE id=?::UUID;"
            val prp: PreparedStatement = connection.prepareStatement(sqlStatement)
            prp.setObject(1, id.unwrap.toJavaUUID()) // postgres doesnt know what a kotlinx.uuid is, needs to be JAVA UUID
            val rs: ResultSet = prp.executeQuery()
            if (!rs.next()) null else
                Restaurant(
                    RestaurantId(rs.getObject("id", java.util.UUID::class.java).toKotlinUUID()),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("food_type"),
                    rs.getString("created_at")
                )
        }
        return rr
    }

    override fun createRestaurant(restaurant: CreateRestaurant): Boolean {
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
            val prp: PreparedStatement = connection.prepareStatement("DELETE FROM restaurants WHERE id=?::UUID;")
            prp.setObject(1, id.unwrap.toJavaUUID())
            prp.executeUpdate()

        }
        return resultInt == 1
    }

    override fun updateRestaurant(updateRestaurant: UpdateRestaurant): Boolean {
        val result: Int = hs.withConnection { connection ->
            val sqlString = "UPDATE restaurants SET name = ?, address = ?, food_type = ? WHERE id = ?::UUID"

            val prep: PreparedStatement = connection.prepareStatement(sqlString)

            prep.setString(1, updateRestaurant.name)
            prep.setString(2, updateRestaurant.address)
            prep.setString(3, updateRestaurant.foodType)
            prep.setObject(4, updateRestaurant.id.unwrap.toJavaUUID())
            prep.executeUpdate()
        }
        return result == 1
    }

    override fun getMenusFromRestaurant(restId: RestaurantId): Collection<Menu> {
        val resultList: MutableList<Menu> = hs.withConnection { connection ->
            val sql: String = "SELECT * FROM menus WHERE restaurant_id=?::UUID;"
            val prp = connection.prepareStatement(sql)
            prp.setObject(1, restId.unwrap.toJavaUUID())
            val rs = prp.executeQuery()

            val menuList = mutableListOf<Menu>()
            while (rs.next()) {
                menuList.add(
                    Menu(
                        MenuId(rs.getObject("id", java.util.UUID::class.java).toKotlinUUID()),
                        RestaurantId(rs.getObject("restaurant_id", java.util.UUID::class.java).toKotlinUUID()),
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
            val sql = "SELECT * FROM menus WHERE id=?::UUID;"
            val prp = connection.prepareStatement(sql)

            prp.setObject(1, menuId.unwrap.toJavaUUID())
            val rs: ResultSet = prp.executeQuery()

            if (!rs.next()) null else

                Menu(
                    MenuId(rs.getObject("id", java.util.UUID::class.java).toKotlinUUID()),
                    RestaurantId(rs.getObject("restaurant_id", java.util.UUID::class.java).toKotlinUUID()),
                    rs.getString("name"),
                    rs.getString("created_at")
                )
        }
        return menu
    }

    override fun createMenu(menu: CreateMenu): Boolean {


        val result = hs.withConnection { connection ->
            val sql = "INSERT INTO menus (restaurant_id, name) VALUES (?::UUID,?);"
            val prp = connection.prepareStatement(sql)
            prp.setObject(1, menu.restaurantId.unwrap.toJavaUUID())
            prp.setString(2, menu.name)
            prp.executeUpdate()
        }
        return result == 1
    }

    override fun deleteMenu(restId: RestaurantId, menuId: MenuId): Boolean {
        val result = hs.withConnection { connection ->
            val prp: PreparedStatement =
                connection.prepareStatement("DELETE FROM menus WHERE restaurant_id=?::UUID AND id=?::UUID;")

            prp.setObject(1, restId.unwrap.toJavaUUID())
            prp.setObject(2, menuId.unwrap.toJavaUUID())
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
                        MenuId(rs.getObject("id", java.util.UUID::class.java).toKotlinUUID()),
                        RestaurantId(rs.getObject("restaurant_id", java.util.UUID::class.java).toKotlinUUID()),
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
            val sql = "UPDATE menus SET name = ? WHERE id = ?::UUID AND restaurant_id = ?::UUID"
            val prp = connection.prepareStatement(sql)

            prp.setString(1, newMenu.name)
            prp.setObject(2, newMenu.menuId.unwrap.toJavaUUID())
            prp.setObject(3, newMenu.restaurantId.unwrap.toJavaUUID())
            prp.executeUpdate()
        }
        return result == 1
    }

    override fun createItem(createItem: CreateItem): Boolean {
        val result = hs.withConnection { connection ->
            val sql = "INSERT INTO items (menu_id, restaurant_id, name, price, description, item_type) VALUES (?::UUID, ?::UUID, ?, ?, ?, ?::item_type);"
            val prp = connection.prepareStatement(sql)

            prp.setObject(1, createItem.menuId.unwrap.toJavaUUID())
            prp.setObject(2, createItem.restaurantId.unwrap.toJavaUUID())
            prp.setString(3, createItem.name)
            prp.setInt(4, createItem.price.unwrap)
            prp.setString(5, createItem.description)
            prp.setString(6, createItem.itemType.toString())
            prp.executeUpdate()
        }
        return result == 1
    }

    override fun getItem(itemId: ItemId): Item? {
        val item = hs.withConnection { connection ->
            val sql = "select * from items where id=?::UUID"
            val prp = connection.prepareStatement(sql)

            prp.setObject(1, itemId.unwrap.toJavaUUID())
            val rs: ResultSet = prp.executeQuery()
            rs.next()
            Item(
                ItemId(rs.getObject("id", java.util.UUID::class.java).toKotlinUUID()),
                MenuId(rs.getObject("menu_id", java.util.UUID::class.java).toKotlinUUID()),
                RestaurantId(rs.getObject("restaurant_id", java.util.UUID::class.java).toKotlinUUID()),
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
            val prp: PreparedStatement = connection.prepareStatement("DELETE FROM items WHERE id=?::UUID and menu_id=?::UUID and restaurant_id=?::UUID;")
            prp.setObject(1, itemId.unwrap.toJavaUUID())
            prp.setObject(2, menuId.unwrap.toJavaUUID())
            prp.setObject(3, restId.unwrap.toJavaUUID())
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
                        ItemId(rs.getObject("id", java.util.UUID::class.java).toKotlinUUID()),
                        MenuId(rs.getObject("menu_id", java.util.UUID::class.java).toKotlinUUID()),
                        RestaurantId(rs.getObject("restaurant_id", java.util.UUID::class.java).toKotlinUUID()),
                        rs.getString("name"),
                        rs.getString("description"),
                        Cents(rs.getInt("price")),
                        rs.getObject("item_type",ItemType::class.java),
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
            prp.setObject(1, updateItem.menuId.unwrap.toJavaUUID())
            prp.setObject(2, updateItem.restaurantId.unwrap.toJavaUUID())
            prp.setString(3, updateItem.name)
            prp.setString(4, updateItem.description)
            prp.setInt(5, updateItem.price.unwrap)
            prp.setObject(6, updateItem.itemType)
            prp.setObject(7,updateItem.itemId.unwrap.toJavaUUID())
            prp.executeUpdate() == 1
        }
        return result
    }

    override fun getItemsByMenu(menuId: MenuId): Collection<Item> {
        val result = hs.withConnection {
            connection ->
            val sql = "select * from items where menu_id=?::UUID"
            val prp = connection.prepareStatement(sql)

            prp.setObject(1,menuId.unwrap.toJavaUUID())
            val rs = prp.executeQuery()

            val itemList = mutableListOf<Item>()
            while (rs.next()) {
                itemList.add(
                    Item(
                        ItemId(rs.getObject("id", java.util.UUID::class.java).toKotlinUUID()),
                        MenuId(rs.getObject("menu_id", java.util.UUID::class.java).toKotlinUUID()),
                        RestaurantId(rs.getObject("restaurant_id", java.util.UUID::class.java).toKotlinUUID()),
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

            prp.setObject(1,restaurantId.unwrap.toJavaUUID())
            val rs = prp.executeQuery()

            val itemList = mutableListOf<Item>()
            while (rs.next()) {
                itemList.add(
                    Item(
                        ItemId(rs.getObject("id", java.util.UUID::class.java).toKotlinUUID()),
                        MenuId(rs.getObject("menu_id", java.util.UUID::class.java).toKotlinUUID()),
                        RestaurantId(rs.getObject("restaurant_id", java.util.UUID::class.java).toKotlinUUID()),
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
