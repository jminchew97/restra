package io.github.jminchew97.models

import io.github.jminchew97.UuidAsStringSerializer
import kotlinx.serialization.Serializable
import java.util.*


@Serializable(with = UuidAsStringSerializer::class)
@JvmInline
value class RestaurantId(val unwrap:UUID)

@Serializable(with = UuidAsStringSerializer::class)
@JvmInline
value class ItemId(val unwrap: UUID)

@Serializable(with = UuidAsStringSerializer::class)
@JvmInline
value class MenuId(val unwrap: UUID)