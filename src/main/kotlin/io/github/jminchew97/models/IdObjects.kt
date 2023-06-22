package io.github.jminchew97.models

import kotlinx.serialization.Serializable


@Serializable
@JvmInline
value class RestaurantId(val unwrap: String)

@Serializable
@JvmInline
value class ItemId(val unwrap: String)

@Serializable
@JvmInline
value class MenuId(val unwrap: String)