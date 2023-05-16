package io.github.jminchew97.models

import kotlinx.serialization.Serializable

@Serializable
data class Menu(val id: MenuId, val restaurantId: RestaurantId)

@Serializable
data class CreateMenu(val restaurantId: RestaurantId)
@Serializable
@JvmInline
value class MenuId(val unwrap: String)

