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
            RestaurantId("oasdoiaj10293u"),
            "oiasjd12", "123 avenue", "asdasdd")

        val s = Json.encodeToString(r)
        val expectedJson = """{"id":"oasdoiaj10293u","menuId":"oiasjd12","name":"123 avenue","address":"asdasdd"}"""
        assertEquals(expectedJson, s)

        assertEquals(r, Json.decodeFromString(s))
    }
}