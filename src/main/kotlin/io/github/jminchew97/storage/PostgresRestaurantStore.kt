package io.github.jminchew97.storage

import io.github.jminchew97.HikariService
import io.github.jminchew97.models.*
import java.sql.PreparedStatement
import java.sql.ResultSet

class PostgresRestaurantStore(private val hs: HikariService) : RestaurantStore {

    override fun getRestaurants(): Collection<Restaurant> {
        val resultList = mutableListOf<Restaurant>()
        hs.withConnection { connection ->
            val sqlStatement: String = "SELECT * FROM restaurant;"
            val pst: PreparedStatement = connection.prepareStatement(sqlStatement)
            val rs: ResultSet = pst.executeQuery()

            while (rs.next()) {
                val rr: Restaurant = Restaurant(
                    RestaurantId(rs.getString("id")),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("food_type")
                )
                resultList.add(rr)
            }
            resultList
        }
        return resultList
    }

    override fun getRestaurant(id: RestaurantId): Restaurant? {
        val rr: Restaurant = hs.withConnection { connection ->
            val sqlStatement: String = "SELECT * FROM restaurant WHERE id='${id.unwrap}';"
            val pst: PreparedStatement = connection.prepareStatement(sqlStatement)
            val rs: ResultSet = pst.executeQuery()

            rs.next()
            val rr: Restaurant = Restaurant(

                RestaurantId(rs.getString("id")),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("food_type")
            )

            rr

        }
        return rr
    }

    override fun createRestaurant(restaurant: CreateRestaurant): Boolean {
//        INSERT INTO restaurant (name, address,food_type)
//        VALUES ('Burger Palace', '123 westore ave', 'american');
        val resultInt = hs.withConnection { connection ->

            val prp: PreparedStatement =
                connection.prepareStatement("INSERT INTO restaurant (name, address,food_type) VALUES ('${restaurant.name}','${restaurant.address}',' ${restaurant.foodType}');")
            prp.executeUpdate()
        }
        println("This is the returned value $resultInt")
        return resultInt == 1
    }

    override fun deleteRestaurant(id: RestaurantId): Boolean {
        val resultInt = hs.withConnection { connection ->
            val prp: PreparedStatement = connection.prepareStatement("DELETE FROM restaurant WHERE id=${id.unwrap}")
            prp.executeUpdate()
        }
        return resultInt == 1
    }


}

