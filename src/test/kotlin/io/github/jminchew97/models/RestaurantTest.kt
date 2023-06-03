package io.github.jminchew97.models

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class RestaurantTest {
    @Test
    fun testJson(){
        val r = Restaurant(
            RestaurantId("1"),
             "Burger Palace", "123 avenue","asd","asd")

        val s = Json.encodeToString(r)
        val expectedJson = """{"id":"1","name":"Burger Palace","address":"123 avenue","foodType":"asd","dateCreated":"asd"}"""
        assertEquals(expectedJson, s)

        assertEquals(r, Json.decodeFromString(s))
    }
}
