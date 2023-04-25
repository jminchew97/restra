package io.github.jminchew97.models

import kotlinx.serialization.Serializable

@Serializable
data class Menu(val id: RestaurantId, val menuId: String, val name: String, val address: String)

@Serializable
@JvmInline
value class MenuId(val unwrap: String)

//Temp list acting as database until we set up database
val menuStorage = mutableListOf<Restaurant>()