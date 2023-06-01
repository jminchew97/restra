package io.github.jminchew97

import io.github.jminchew97.models.Restaurant
import io.github.jminchew97.models.RestaurantId
import java.sql.ResultSet

fun restaurantsResultSetToList(rs: ResultSet): Collection<Restaurant>{
    val resultList = mutableListOf<Restaurant>()
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
    return resultList
}
