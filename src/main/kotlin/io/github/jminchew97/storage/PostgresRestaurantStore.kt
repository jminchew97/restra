package io.github.jminchew97.storage

import io.github.jminchew97.HikariService
import io.github.jminchew97.models.*
import java.sql.PreparedStatement
import java.sql.ResultSet

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
            prp.setInt(1, id.unwrap.toInt())
            val rs: ResultSet = prp.executeQuery()



            if (!rs.next()) {
                return@withConnection null
            }

            val rr: Restaurant? = Restaurant(
                RestaurantId(rs.getString("id")),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("food_type"),
                rs.getString("created_at")
            )
            rr


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
            prp.setInt(1, id.unwrap.toInt())
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
            prep.setInt(4, updateRest.id.unwrap.toInt())
            prep.executeUpdate()
        }
        return result == 1
    }

    override fun getMenusFromRestaurant(restId: RestaurantId): Collection<Menu> {
        val resultList: MutableList<Menu> = hs.withConnection { connection ->
            val sql: String = "SELECT * FROM menus WHERE restaurant_id=?;"
            val prp = connection.prepareStatement(sql)
            prp.setInt(1, restId.unwrap.toInt())
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

            prp.setInt(1, menuId.unwrap.toInt())
            val rs: ResultSet = prp.executeQuery()

            if (!rs.next()) {
                return@withConnection null
            }

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

            prp.setInt(1, menu.restaurantId.unwrap.toInt())
            prp.setString(2, menu.name)
            prp.executeUpdate()
        }
        println("result of inset menu statement: $result")
        return result == 1
    }

    override fun deleteMenu(restId: RestaurantId, menuId: MenuId): Boolean {
        val result = hs.withConnection { connection ->
            val prp: PreparedStatement =
                connection.prepareStatement("DELETE FROM menus WHERE restaurant_id=? AND id=?;")

            prp.setInt(1, restId.unwrap.toInt())
            prp.setInt(2, menuId.unwrap.toInt())
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

    override fun updateMenu(newMenu: UpdateMenu): Boolean {
        val result = hs.withConnection { connection ->
            val sql = "UPDATE menus SET name = ? WHERE id = ? AND restaurant_id = ?"
            val prp = connection.prepareStatement(sql)

            prp.setString(1, newMenu.name)
            prp.setInt(2, newMenu.menuId.unwrap.toInt())
            prp.setInt(3, newMenu.restaurantId.unwrap.toInt())
            prp.executeUpdate()
        }
        return result == 1
    }

    override fun createItem(createItem: CreateItem): Boolean {
        val result = hs.withConnection { connection ->
            val sql = "INSERT INTO items (menu_id, name, price, description, item_type) VALUES (?,?,?,?,?);"
            val prp = connection.prepareStatement(sql)

            prp.setInt(1, createItem.menuId.unwrap.toInt())
            prp.setString(2, createItem.name)
            prp.setInt(3, createItem.price.unwrap)
            prp.setString(4, createItem.description)

            prp.executeUpdate()
        }
        return result == 1
    }
}
