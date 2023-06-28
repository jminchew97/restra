package io.github.jminchew97

import java.util.*

fun String.toUuid():UUID{
    return UUID.fromString(this)
}