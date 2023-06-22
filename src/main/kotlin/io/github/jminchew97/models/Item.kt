package io.github.jminchew97.models

import kotlinx.serialization.Serializable
@Serializable
data class CreateItem(
    val menuId: MenuId,
    val name: String,
    val description: String,
    val price: Cents
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
    val price: String
)

