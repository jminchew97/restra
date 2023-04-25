package io.github.jminchew97.storage

import io.github.jminchew97.models.Restaurant
import io.github.jminchew97.models.RestaurantId

class InMemoryRestaurantStore : RestaurantStore {
    private val internal = mutableMapOf<RestaurantId, Restaurant>()
    override fun getRestaurants(): Collection<Restaurant> {
        return internal.values
    }

    override fun getRestaurant(id: RestaurantId): Restaurant? {
        return internal[id]
    }

    override fun deleteRestaurant(id: RestaurantId): Boolean {

        return internal.remove(id) != null
    }

    override fun createRestaurant(restaurant: Restaurant): Boolean {

        if (internal.containsKey(restaurant.id)) return false // restaurant already exists

        internal[restaurant.id] = restaurant // add restaurant
        return true

    }


}