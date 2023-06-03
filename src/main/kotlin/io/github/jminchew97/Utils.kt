package io.github.jminchew97

import io.github.jminchew97.models.CreateRestaurant
import io.github.jminchew97.models.Restaurant
import io.github.jminchew97.models.RestaurantId
import java.sql.ResultSet

fun convertRestaurantToCreateRestaurant(rs: Restaurant?): CreateRestaurant? {

    if (rs != null) {
        return CreateRestaurant(rs.name, rs.address,rs.foodType)
    }
    return null
}
