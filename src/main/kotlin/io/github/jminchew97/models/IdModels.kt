package io.github.jminchew97.models


import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID


@Serializable
@JvmInline
value class RestaurantId(val unwrap:UUID)

@Serializable
@JvmInline
value class ItemId(val unwrap: UUID)

@Serializable
@JvmInline
value class MenuId(val unwrap: UUID)