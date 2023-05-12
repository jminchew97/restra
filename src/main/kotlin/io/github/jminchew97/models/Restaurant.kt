package io.github.jminchew97.models

import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(val id: RestaurantId, val name: String, val address: String)

@Serializable
@JvmInline
value class RestaurantId(val unwrap: String)
