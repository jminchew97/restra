package io.github.jminchew97.utils

fun String.isDigit(): Boolean{
    return this.matches(Regex("\\d+"))
}