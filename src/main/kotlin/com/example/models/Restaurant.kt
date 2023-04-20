package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(val id: String, val menuId: String, val name: String, val address: String)
val restaurantStorage = mutableListOf<Restaurant>()