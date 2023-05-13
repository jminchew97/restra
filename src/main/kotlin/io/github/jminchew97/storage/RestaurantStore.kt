package io.github.jminchew97.storage

import io.github.jminchew97.models.*

interface RestaurantStore {
    fun getRestaurants(): Collection<Restaurant>

    fun getRestaurant(id: RestaurantId) : Restaurant?

    fun createRestaurant(restaurant: CreateRestaurant) : Boolean
    fun deleteRestaurant(id: RestaurantId): Boolean

//    fun addMenuItem(id: MenuId): Boolean
//    fun clearMenuItems(id: MenuId) : Boolean
//    fun removeMenuItem(id: menuId)

}
