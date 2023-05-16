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
            val pst: PreparedStatement = connection.prepareStatement(sqlStatement)
            val rs: ResultSet = pst.executeQuery()

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

            val sqlStatement: String = "SELECT * FROM restaurants WHERE id='${id.unwrap}';"
            val pst: PreparedStatement = connection.prepareStatement(sqlStatement)
            val rs: ResultSet = pst.executeQuery()



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
                connection.prepareStatement("INSERT INTO restaurants (name, address,food_type) VALUES ('${restaurant.name}','${restaurant.address}',' ${restaurant.foodType}');")
            prp.executeUpdate()
        }
        return resultInt == 1
    }

    override fun deleteRestaurant(id: RestaurantId): Boolean {
        val resultInt = hs.withConnection { connection ->
            val prp: PreparedStatement = connection.prepareStatement("DELETE FROM restaurants WHERE id=${id.unwrap}")
            prp.executeUpdate()
        }
        return resultInt == 1
    }

    override fun updateRestaurant(cr: UpdateRestaurant): Boolean {
        val result: Int = hs.withConnection { connection ->
            val sqlString = "UPDATE restaurants SET name = ?, address = ?, food_type = ? WHERE id = ?"

            val prep: PreparedStatement = connection.prepareStatement(sqlString)

            prep.setString(1, cr.name)
            prep.setString(2, cr.address)
            prep.setString(3, cr.foodType)
            prep.setInt(4, cr.id.unwrap.toInt())
            prep.executeUpdate()
        }
        return result == 1
    }

    override fun getMenusFromRestaurant(restId: RestaurantId): Collection<Menu> {
        val resultList: MutableList<Menu> = hs.withConnection { connection ->
            val sql: String = "SELECT * FROM menus WHERE restaurant_id=${restId.unwrap.toInt()};"
            val prp = connection.prepareStatement(sql)
            val rs = prp.executeQuery()

            val menuList = mutableListOf<Menu>()
            while (rs.next()) {
                menuList.add(
                    Menu(
                        MenuId(rs.getInt("id").toString()),
                        RestaurantId(rs.getInt("restaurant_id").toString())
                    )
                )
            }
            menuList
        }
        return resultList
    }

    override fun getMenu(menuId: MenuId): Menu? {
        val menu:Menu? = hs.withConnection { connection ->
            val sql = "SELECT * FROM menus WHERE id='${menuId.unwrap}';"
            val prp = connection.prepareStatement(sql)
            val rs:ResultSet = prp.executeQuery()

            if (!rs.next()){
                return@withConnection null
            }

            Menu(
                MenuId(rs.getInt("id").toString()),
                RestaurantId(rs.getInt("restaurant_id").toString())
            )
        }
        return menu
    }

    override fun createMenu(menu: CreateMenu): Boolean {
        //TODO finish this function
        val result = hs.withConnection {
            connection ->
            val sql = "INSERT INTO menus (restaurant_id) VALUES (${menu.restaurantId.unwrap})"
            val prp = connection.prepareStatement(sql)

            prp.executeUpdate()
        }

        return result == 1
    }


}

