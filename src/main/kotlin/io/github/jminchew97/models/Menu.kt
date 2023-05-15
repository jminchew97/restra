package io.github.jminchew97.models

import kotlinx.serialization.Serializable

@Serializable
data class Menu(val id: MenuId, val restaurantId: String)

@Serializable
@JvmInline
value class MenuId(val unwrap: String)

