package io.github.jminchew97.models

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class RestaurantTest {
    @Test
    fun testJson(){
        val r = Restaurant(
            RestaurantId("1"),
             "Burger Palace", "123 avenue")

        val s = Json.encodeToString(r)
        val expectedJson = """{"id":"1","name":"Burger Palace","address":"123 avenue"}"""
        assertEquals(expectedJson, s)

        assertEquals(r, Json.decodeFromString(s))
    }
}
