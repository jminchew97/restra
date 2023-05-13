package io.github.jminchew97.models

import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(val id: RestaurantId, val name: String, val address: String, val foodType:String)

@Serializable
//Has no ID because object has not been entered into database yet
data class CreateRestaurant(val name: String, val address: String, val foodType:String)
@Serializable
@JvmInline
value class RestaurantId(val unwrap: String)
