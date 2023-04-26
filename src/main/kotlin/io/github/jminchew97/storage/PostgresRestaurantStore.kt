package io.github.jminchew97.storage

import io.github.jminchew97.HikariService
import io.github.jminchew97.models.Restaurant
import io.github.jminchew97.models.RestaurantId
class PostgresRestaurantStore(hks : HikariService) : RestaurantStore {
    private val hks: HikariService = hks
    override fun getRestaurants(): Collection<Restaurant> {
        TODO("Not yet implemented")
    }

    override fun getRestaurant(id: RestaurantId): Restaurant? {
        TODO("Not yet implemented")
    }

    override fun createRestaurant(restaurant: Restaurant): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteRestaurant(id: RestaurantId): Boolean {
        TODO("Not yet implemented")
    }
}