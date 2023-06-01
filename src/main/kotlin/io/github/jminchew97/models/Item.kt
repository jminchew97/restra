package io.github.jminchew97.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class Item(
    val id: ItemId,
    val menuId: MenuId,
    val name: String,
    val price: @Contextual BigDecimal,
    val description: String,
    val itemType: ItemType
)

@Serializable
data class CreateItem(
    val menuId: MenuId,
    val name: String,
    val price: @Contextual BigDecimal,
    val description: String,
    val itemType: ItemType
)


enum class ItemType {
    DESSERT,
    DRINK,
    ENTREE,
    APPETIZER,
    ALCOHOLIC_DRINK
}

