package io.github.jminchew97.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.*

@Serializable
data class CreateItem(
    val restaurantId: RestaurantId,
    val menuId: MenuId,
    val name: String,
    val description: String,
    val price: Cents,
    val itemType: String
)
@Serializable
data class UpdateItem(
    val itemId:ItemId,
    val restaurantId: RestaurantId,
    val menuId: MenuId,
    val name: String,
    val description: String,
    val price: Cents,
    val itemType: String
)
@Serializable
/**
Cannot deserialize JSON directly to CreateItem since the required field menuId
is in the URI. Must use CreateItemReceive (excludes menuId) then convert to
CreateItem and include the menuId from URI variable.
 */

data class CreateItemReceive(
    val name: String,
    val description: String,
    val price: String,
    val itemType: String
)

@Serializable
data class Item(
    val id: ItemId,
    val menuId: MenuId,
    val restaurantId: RestaurantId,
    val name: String,
    val description: String,
    val price: Cents,
    val itemType: ItemType,
    val createAt:Instant
)



