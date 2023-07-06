package io.github.jminchew97.models

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID

class RestaurantTest {
    @Test
    fun testJson(){
        val id = UUID.generateUUID()
        val r = Restaurant(
            RestaurantId(id),
             "Burger Palace", "123 avenue","asd","asd")

        val s = Json.encodeToString(r)
        val expectedJson = """{"id":"$id","name":"Burger Palace","address":"123 avenue","foodType":"asd","dateCreated":"asd"}"""
        assertEquals(expectedJson, s)

        assertEquals(r, Json.decodeFromString(s))
    }
}
