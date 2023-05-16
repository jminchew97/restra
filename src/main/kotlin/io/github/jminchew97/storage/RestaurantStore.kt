package io.github.jminchew97.storage

import io.github.jminchew97.models.*

interface RestaurantStore {
    fun getRestaurants(): Collection<Restaurant>
    fun getRestaurant(id: RestaurantId): Restaurant?
    fun createRestaurant(restaurant: CreateRestaurant): Boolean
    fun deleteRestaurant(id: RestaurantId): Boolean
    fun updateRestaurant(cr: UpdateRestaurant): Boolean
    fun getMenusFromRestaurant(restId: RestaurantId): Collection<Menu>
    fun getMenu(menuId: MenuId): Menu?
    fun createMenu(menu: CreateMenu): Boolean

}
