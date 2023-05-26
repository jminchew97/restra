package io.github.jminchew97.models

import kotlinx.serialization.Serializable

@Serializable
data class Menu(val id: MenuId, val restaurantId: RestaurantId,val name: String, val createdAt: String)
@Serializable
data class UpdateMenu(val restaurantId: RestaurantId, val menuId: MenuId, val name: String)
@Serializable
data class UpdateMenuReceive(val name: String)

@Serializable
data class CreateMenu(var restaurantId: RestaurantId, val name: String)
@Serializable
@JvmInline
value class MenuId(val unwrap: String)

