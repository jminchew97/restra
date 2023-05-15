package io.github.jminchew97.models

import kotlinx.serialization.Serializable

@Serializable
// Only used for retrieving already existing restaurant
data class Restaurant(val id: RestaurantId, val name: String, val address: String, val foodType:String, val createdAt:String)


@Serializable
/**
 * Used for updating existing `Restaurant` in the database, exclude any properties that should not be changed:
 *  e.g `createdAt`. You need the `id` in order to actually retrieve the restaurant from the database, but
 * the `id` will not be edited.
 */
data class UpdateRestaurant(val id: RestaurantId, val name: String, val address: String, val foodType:String)

@Serializable
//This dataclass has no ID because object has not been entered into database yet

data class CreateRestaurant(val name: String, val address: String, val foodType:String)

@Serializable
@JvmInline
value class RestaurantId(val unwrap: String)
