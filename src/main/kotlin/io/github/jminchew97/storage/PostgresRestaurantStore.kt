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
                    rs.getString("address")
                )
                resultList.add(rr)
            }
            resultList
        }
        return resultList
    }

    override fun getRestaurant(id: RestaurantId): Restaurant? {
        val rr: Restaurant = hs.withConnection { connection ->
            val sqlStatement: String = "SELECT * FROM TABLE restaurant WHERE ID IN ($id)"
            val pst: PreparedStatement = connection.prepareStatement(sqlStatement)
            val rs: ResultSet = pst.executeQuery()


            val rr: Restaurant = Restaurant(
                RestaurantId(rs.getString("id")),
                rs.getString("name"),
                rs.getString("address")
            )
            print("RESTAURANT OUTPUT tEST: $rr")
            rr

        }
        return rr
    }

    override fun createRestaurant(restaurant: Restaurant): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteRestaurant(id: RestaurantId): Boolean {
        TODO("Not yet implemented")
    }
}

