package io.github.jminchew97.models

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Cents(val unwrap: Int)
