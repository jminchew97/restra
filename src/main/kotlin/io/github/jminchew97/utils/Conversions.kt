package io.github.jminchew97.utils

import io.github.jminchew97.models.Cents
import java.math.BigDecimal

fun Cents.Companion.convertStringToCents(money: String):Cents{
    val centsInDecimal = BigDecimal(money) * BigDecimal(100)
    return Cents(
        centsInDecimal.toInt()
    )
}
